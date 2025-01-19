package com.robotbot.financetracker.presentation.bank_account.transfer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotbot.financetracker.domain.entities.BankAccountEntity
import com.robotbot.financetracker.domain.entities.Currency
import com.robotbot.financetracker.domain.usecases.GetCurrencyRatesUseCase
import com.robotbot.financetracker.domain.usecases.ConvertAmountBetweenCurrencies
import com.robotbot.financetracker.domain.usecases.account.GetBankAccountUseCase
import com.robotbot.financetracker.domain.usecases.transfer.AddTransferUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject


class CreateTransferViewModel @Inject constructor(
    private val getBankAccountUseCase: GetBankAccountUseCase,
    private val addTransferUseCase: AddTransferUseCase,
    private val getCurrencyRatesUseCase: GetCurrencyRatesUseCase,
    private val convertAmountBetweenCurrencies: ConvertAmountBetweenCurrencies
) : ViewModel() {

    private val _state = MutableStateFlow(CreateTransferState())
    val state = _state.asStateFlow()

    private var currencyRates: Map<Currency, BigDecimal>? = null

    private val _amountFrom = MutableSharedFlow<String>()
    private val amountFrom = _amountFrom.map {
        it.toValidAmountOrNull()
    }.onEach { amountFrom ->
        _state.update {
            it.copy(amountFrom = amountFrom)
        }
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
        getCurrencyRatesUseCase.realInvoke(
            listOf(
                Currency.USD,
                Currency.RUB,
                Currency.BTC,
                Currency.EUR
            )
        ).retry(RETRY_GET_CURRENCY_RATES_ATTEMPTS) {
            delay(RETRY_GET_CURRENCY_RATES_DELAY_IN_MILLIS)
            true
        }
            .catch {
                Log.w(
                    LOG_TAG,
                    "Can't get currency rates with exception: ${it.message}"
                )
            }
            .onEach {
                currencyRates = it
            }.launchIn(viewModelScope)
    }

    private fun setAmountPlaceholderForSecondAccount() {
        combine(
            amountFrom,
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
                    convertAmountBetweenCurrencies(
                        amount = firstAmount,
                        currencyPair = currencyFrom to currencyTo,
                        currencyRates = currentCurrencyRates
                    )
                }
            }
        }.onEach { amountToPlaceholder ->
            _state.update { currentState ->
                val newDisplayState =
                    if (currentState.displayState is CreateTransferDisplayState.DifferentCurrencies) {
                        currentState.displayState.copy(
                            amountToPlaceholder = amountToPlaceholder
                        )
                    } else {
                        CreateTransferDisplayState.DifferentCurrencies(amountToPlaceholder)
                    }
                currentState.copy(displayState = newDisplayState)
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
        val amountFrom = currentState.amountFrom

        if (accountFrom == null || accountTo == null || amountFrom == null) {
            setErrorInState(ErrorState.InvalidTransfer)
            return
        }

        val amountTo =
            if (currentState.displayState is CreateTransferDisplayState.DifferentCurrencies) {
                currentState.displayState.amountTo ?: currentState.displayState.amountToPlaceholder
            } else {
                amountFrom
            }

        viewModelScope.launch {
            val result = addTransferUseCase(
                accountFrom = accountFrom,
                accountTo = accountTo,
                amountFrom = amountFrom,
                amountTo = amountTo
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

    fun setAmountFrom(amountInput: String) {
        viewModelScope.launch {
            _amountFrom.emit(amountInput)
        }
    }

    fun setAmountTo(amountInput: String) {
        viewModelScope.launch {
            val currentState = state.value
            if (currentState.displayState is CreateTransferDisplayState.DifferentCurrencies) {
                _state.update {
                    currentState.copy(displayState = currentState.displayState.copy(amountTo = amountInput.toValidAmountOrNull()))
                }
            }
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

    companion object {
        private const val LOG_TAG = "CreateTransferViewModel"
        private const val RETRY_GET_CURRENCY_RATES_DELAY_IN_MILLIS = 3000L
        private const val RETRY_GET_CURRENCY_RATES_ATTEMPTS = 3L
    }
}