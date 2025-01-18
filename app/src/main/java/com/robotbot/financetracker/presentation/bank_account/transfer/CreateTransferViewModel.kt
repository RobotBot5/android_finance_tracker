package com.robotbot.financetracker.presentation.bank_account.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotbot.financetracker.domain.entities.BankAccountEntity
import com.robotbot.financetracker.domain.entities.Currency
import com.robotbot.financetracker.domain.usecases.GetCurrencyRatesUseCase
import com.robotbot.financetracker.domain.usecases.TransferBetweenCurrencies
import com.robotbot.financetracker.domain.usecases.account.GetBankAccountUseCase
import com.robotbot.financetracker.domain.usecases.transfer.AddTransferUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

class CreateTransferViewModel @Inject constructor(
    private val getBankAccountUseCase: GetBankAccountUseCase,
    private val addTransferUseCase: AddTransferUseCase,
    private val getCurrencyRatesUseCase: GetCurrencyRatesUseCase,
    private val transferBetweenCurrencies: TransferBetweenCurrencies
) : ViewModel() {

    private val _state = MutableStateFlow(CreateTransferState())
    val state = _state.asStateFlow()

    private var currencyRates: Map<Currency, BigDecimal>? = null

    private val _amount = MutableSharedFlow<String>()
    private val amount = _amount.map {
        it.toValidAmountOrNull()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    private val updatePlaceholderForSecondAccountEvents = MutableSharedFlow<Unit>()

    init {
        loadCurrencyRates()
        setAmountPlaceholderForSecondAccount()
    }

    private fun loadCurrencyRates() {
        viewModelScope.launch {
            currencyRates = getCurrencyRatesUseCase.realInvoke(
                listOf(
                    Currency.USD,
                    Currency.RUB,
                    Currency.BTC,
                    Currency.EUR
                )
            )
        }
    }

    private fun setAmountPlaceholderForSecondAccount() {
        combine(
            amount,
            updatePlaceholderForSecondAccountEvents
        ) { firstAmount, _ ->
            val currentState = state.value
            if (currentState.displayState is CreateTransferDisplayState.SingleCurrency || firstAmount == null) {
                BigDecimal.ZERO
            } else {
                val currencyFrom = currentState.accountFrom?.currency
                val currencyTo = currentState.accountTo?.currency
                val currentCurrencyRates = currencyRates
                if (currencyFrom == null || currencyTo == null || currentCurrencyRates == null) {
                    BigDecimal.ZERO
                } else {
                    transferBetweenCurrencies(
                        amount = firstAmount,
                        currencyPair = currencyFrom to currencyTo,
                        currencyRates = currentCurrencyRates
                    )
                }
            }
        }.map {
            it.toPlainString()
        }.onEach { amountToPlaceholder ->
            _state.update {
                it.copy(
                    displayState = CreateTransferDisplayState.DifferentCurrencies(
                        amountToPlaceholder
                    )
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun String.toValidAmountOrNull(): BigDecimal? {
        val amount = this.trim()
        if (amount.isNotBlank()) {
            amount.toBigDecimalOrNull()
                ?.let { if (it > BigDecimal.ZERO) return it }
        }
        return null
    }

    private fun setErrorInState(errorState: ErrorState) {
        _state.update {
            it.copy(displayState = CreateTransferDisplayState.Error(errorState))
        }
    }

    fun saveTransfer() {
        val currentState = state.value
        val accountFrom = currentState.accountFrom
        val accountTo = currentState.accountTo
        val currentAmount = amount.value
        if (accountFrom == null || accountTo == null || currentAmount == null) {
            setErrorInState(ErrorState.InvalidTransfer)
            return
        }

        viewModelScope.launch {
            val result = addTransferUseCase(
                accountFrom = accountFrom,
                accountTo = accountTo,
                amount = currentAmount
            )
            when (result) {
                AddTransferUseCase.Result.InsufficientFunds -> {
                    setErrorInState(ErrorState.InsufficientFunds)
                }

                AddTransferUseCase.Result.InvalidTransfer -> {
                    setErrorInState(ErrorState.InvalidTransfer)
                }

                AddTransferUseCase.Result.Success -> {
                    _state.update {
                        it.copy(displayState = CreateTransferDisplayState.WorkEnded)
                    }
                }

                is AddTransferUseCase.Result.UnknownError -> {
                    setErrorInState(ErrorState.UnknownError)
                }
            }
        }
    }

    fun setAmount(amountInput: String) {
        viewModelScope.launch {
            _amount.emit(amountInput)
        }
    }

    fun setFromAccount(bankAccountId: Int) {
        setAccount(bankAccountId = bankAccountId, isFromAccount = true)
    }

    fun setToAccount(bankAccountId: Int) {
        setAccount(bankAccountId = bankAccountId, isFromAccount = false)
    }

    private fun setAccount(bankAccountId: Int, isFromAccount: Boolean) {
        viewModelScope.launch {
            val bankAccount = getBankAccountUseCase(bankAccountId)
            _state.update { currentState ->
                val isSingleCurrency = isSingleCurrency(currentState, bankAccount, isFromAccount)
                if (isSingleCurrency) {
                    currentState.createUpdatedState(
                        bankAccount = bankAccount,
                        isFromAccount = isFromAccount,
                        newDisplayState = CreateTransferDisplayState.SingleCurrency
                    )
                } else {
                    updatePlaceholderForSecondAccountEvents.emit(Unit)
                    currentState.createUpdatedState(
                        bankAccount = bankAccount,
                        isFromAccount = isFromAccount,
                        newDisplayState = null
                    )
                }
            }
        }
    }

    private fun isSingleCurrency(
        currentState: CreateTransferState,
        bankAccount: BankAccountEntity,
        isFromAccount: Boolean
    ): Boolean {
        val targetCurrency = if (isFromAccount) {
            currentState.accountTo?.currency
        } else {
            currentState.accountFrom?.currency
        }
        return targetCurrency == null || targetCurrency == bankAccount.currency
    }

    private fun CreateTransferState.createUpdatedState(
        bankAccount: BankAccountEntity,
        isFromAccount: Boolean,
        newDisplayState: CreateTransferDisplayState?
    ): CreateTransferState {
        return if (isFromAccount) {
            copy(
                accountFrom = bankAccount,
                accountTo = if (accountTo == bankAccount) null else accountTo,
                displayState = newDisplayState ?: displayState
            )
        } else {
            copy(
                accountFrom = if (accountFrom == bankAccount) null else accountFrom,
                accountTo = bankAccount,
                displayState = newDisplayState ?: displayState
            )
        }
    }


}