package com.robotbot.financetracker.presentation.bank_account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotbot.financetracker.domain.entities.BankAccountEntity
import com.robotbot.financetracker.domain.entities.Currency
import com.robotbot.financetracker.domain.usecases.GetCurrencyRatesUseCase
import com.robotbot.financetracker.domain.usecases.account.GetBankAccountListUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class BankAccountViewModel @Inject constructor(
    getBankAccountListUseCase: GetBankAccountListUseCase,
    private val getCurrencyRatesUseCase: GetCurrencyRatesUseCase
) : ViewModel() {

    private val _bankAccountListState: Flow<BankAccountListState> = getBankAccountListUseCase()
        .onEach {
            calculateTotalBalance(it)
        }
        .map {
            BankAccountListState.Content(it)
        }

    val test = Random.nextInt()

    init {
        Log.d("viewModelTest", "init $test")
    }

    private val _totalBalanceState: MutableSharedFlow<TotalBalanceState> = MutableSharedFlow()

    val state: StateFlow<BankAccountState> = combine(
        _bankAccountListState,
        _totalBalanceState
    ) { bankAccountListState, totalBalanceState ->
        BankAccountState(
            bankAccountListState = bankAccountListState,
            totalBalanceState = totalBalanceState
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = BankAccountState(
            bankAccountListState = BankAccountListState.Loading,
            totalBalanceState = TotalBalanceState.Loading
        )
    )

    private fun calculateTotalBalance(accounts: List<BankAccountEntity>) {
        viewModelScope.launch {
            _totalBalanceState.emit(TotalBalanceState.Loading)
            try {
                val currencyRates = getCurrencyRatesWithRetry()
                val totalBalance = accounts.sumOf { account ->
                    if (account.currency == Currency.RUB) {
                        account.balance.toDouble()
                    } else {
                        currencyRates[account.currency.name]?.times(account.balance.toDouble())
                            ?: throw RuntimeException()
                    }
                }
                _totalBalanceState.emit(TotalBalanceState.Content(totalBalance))
            } catch (e: Exception) {
                _totalBalanceState.emit(TotalBalanceState.Error)
            }
        }
    }

    private suspend fun getCurrencyRatesWithRetry(): Map<String, Double> {
        val retryDelayInMillis = 3000L
        val maxAttempts = 3
        repeat(maxAttempts) {
            try {
                return getCurrencyRatesUseCase()
            } catch (e: Exception) {
                if (it == maxAttempts - 1) {
                    throw e
                }
                delay(retryDelayInMillis)
            }
        }
        throw RuntimeException()
    }

    fun retryLoadBalance() {
        val currentState = state.value.bankAccountListState
        if (currentState !is BankAccountListState.Content) {
            return
        }
        val accounts = currentState.accounts
        calculateTotalBalance(accounts)
    }

}