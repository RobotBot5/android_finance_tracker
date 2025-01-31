package com.robotbot.financetracker.presentation.bank_account.transfer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotbot.financetracker.domain.entities.BankAccountEntity
import com.robotbot.financetracker.domain.entities.Currency
import com.robotbot.financetracker.domain.entities.TransferEntity
import com.robotbot.financetracker.domain.usecases.GetCurrencyRatesUseCase
import com.robotbot.financetracker.domain.usecases.ConvertAmountBetweenCurrencies
import com.robotbot.financetracker.domain.usecases.account.GetBankAccountUseCase
import com.robotbot.financetracker.domain.usecases.transfer.AddTransferUseCase
import com.robotbot.financetracker.domain.usecases.transfer.DeleteTransferUseCase
import com.robotbot.financetracker.domain.usecases.transfer.GetTransferUseCase
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject


class CreateTransferViewModel @Inject constructor(
    private val getBankAccountUseCase: GetBankAccountUseCase,
    private val addTransferUseCase: AddTransferUseCase,
    private val getCurrencyRatesUseCase: GetCurrencyRatesUseCase,
    private val convertAmountBetweenCurrencies: ConvertAmountBetweenCurrencies,
    private val getTransferUseCase: GetTransferUseCase,
    private val deleteTransferUseCase: DeleteTransferUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(
        CreateTransferState(
            selectedDate = Calendar.getInstance()
        )
    )
    val state = _state.asStateFlow()

    private var currencyRates: Map<Currency, BigDecimal>? = null

    private var transferToEditOrUndefined: TransferEntity? = null

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
            if (currentState.currencyState is CreateTransferCurrencyState.SingleCurrency || firstAmount == null) {
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
                if (currentState.currencyState is CreateTransferCurrencyState.DifferentCurrencies) {
                    currentState.copy(
                        currencyState = currentState.currencyState.copy(
                            amountToPlaceholder = amountToPlaceholder
                        )
                    )
                } else {
                    currentState
                }
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

    fun formatDateToString(date: Calendar): String =
        SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(date.time)

    fun setDate(date: Calendar) = _state.update { it.copy(selectedDate = date) }

    fun saveTransfer() {
        val currentState = state.value
        val accountFrom = currentState.accountFrom
        val accountTo = currentState.accountTo
        val amountFrom = currentState.amountFrom
        val date = currentState.selectedDate

        if (accountFrom == null || accountTo == null || amountFrom == null) {
            setErrorInState(ErrorState.InvalidTransfer)
            return
        }

        val amountTo =
            if (currentState.currencyState is CreateTransferCurrencyState.DifferentCurrencies) {
                currentState.currencyState.amountTo ?: currentState.currencyState.amountToPlaceholder
            } else {
                amountFrom
            }

        viewModelScope.launch {
            val result = addTransferUseCase(
                accountFrom = accountFrom,
                accountTo = accountTo,
                amountFrom = amountFrom,
                amountTo = amountTo,
                date = date
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
            if (currentState.currencyState is CreateTransferCurrencyState.DifferentCurrencies) {
                _state.update {
                    currentState.copy(currencyState = currentState.currencyState.copy(amountTo = amountInput.toValidAmountOrNull()))
                }
            }
        }
    }

    fun setFromAccount(bankAccountId: Int) {
        viewModelScope.launch {
            setAccount(bankAccountId = bankAccountId, isFromAccount = true)
        }
    }

    fun setToAccount(bankAccountId: Int) {
        viewModelScope.launch {
            setAccount(bankAccountId = bankAccountId, isFromAccount = false)
        }
    }

    private suspend fun setAccount(bankAccountId: Int, isFromAccount: Boolean) {
        val bankAccount = getBankAccountUseCase(bankAccountId)
        _state.update { currentState ->
            val isSingleCurrency = isSingleCurrency(currentState, bankAccount, isFromAccount)
            if (isSingleCurrency) {
                currentState.createUpdatedState(
                    bankAccount = bankAccount,
                    isFromAccount = isFromAccount,
                    newDisplayState = CreateTransferCurrencyState.SingleCurrency
                )
            } else {
                val newDisplayState =
                    if (currentState.currencyState is CreateTransferCurrencyState.DifferentCurrencies) {
                        currentState.currencyState
                    } else {
                        CreateTransferCurrencyState.DifferentCurrencies(
                            amountToPlaceholder = BigDecimal.ZERO,
                            amountTo = null
                        )
                    }
                currentState.createUpdatedState(
                    bankAccount = bankAccount,
                    isFromAccount = isFromAccount,
                    newDisplayState = newDisplayState
                )
            }
        }
        updatePlaceholderForSecondAccountEvents.emit(Unit)
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
        newDisplayState: CreateTransferCurrencyState
    ): CreateTransferState {
        return if (isFromAccount) {
            copy(
                accountFrom = bankAccount,
                accountTo = if (accountTo == bankAccount) null else accountTo,
                currencyState = newDisplayState
            )
        } else {
            copy(
                accountFrom = if (accountFrom == bankAccount) null else accountFrom,
                accountTo = bankAccount,
                currencyState = newDisplayState
            )
        }
    }

    fun setupTransferToEditById(transferId: Int) {
        viewModelScope.launch {
            _state.update {
                it.copy(displayState = CreateTransferDisplayState.Loading)
            }
            val transferEntity = getTransferUseCase(transferId)
            transferToEditOrUndefined = transferEntity
            setAmountFrom(transferEntity.amountFrom.toPlainString())
            setDate(transferEntity.date)
            setAccount(bankAccountId = transferEntity.accountFrom.id, isFromAccount = true)
            setAccount(bankAccountId = transferEntity.accountTo.id, isFromAccount = false)
            setAmountTo(transferEntity.amountTo.toPlainString())
            _state.update {
                it.copy(displayState = CreateTransferDisplayState.Content)
            }
        }
    }

    fun updateTransfer() {

    }

    fun deleteTransfer() {
        val transferEntityToEdit = transferToEditOrUndefined
        if (transferEntityToEdit == null) {
            _state.update {
                it.copy(displayState = CreateTransferDisplayState.Error(ErrorState.UnknownError))
            }
            return
        }
        viewModelScope.launch {
            deleteTransferUseCase(transferEntityToEdit)
            _state.update {
                it.copy(displayState = CreateTransferDisplayState.WorkEnded)
            }
        }
    }

    companion object {
        private const val LOG_TAG = "CreateTransferViewModel"
        private const val RETRY_GET_CURRENCY_RATES_DELAY_IN_MILLIS = 3000L
        private const val RETRY_GET_CURRENCY_RATES_ATTEMPTS = 3L
    }
}