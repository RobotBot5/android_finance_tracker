package com.robotbot.financetracker.presentation.bank_account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotbot.financetracker.di.RealBankAccountDatabaseQualifier
import com.robotbot.financetracker.domain.entities.BankAccountEntity
import com.robotbot.financetracker.domain.entities.Currency
import com.robotbot.financetracker.domain.repotisories.BankAccountRepository
import com.robotbot.financetracker.domain.usecases.GetCurrencyRatesUseCase
import com.robotbot.financetracker.domain.usecases.account.GetBankAccountListUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

class BankAccountViewModel @Inject constructor(
    getBankAccountListUseCase: GetBankAccountListUseCase,
    private val getCurrencyRatesUseCase: GetCurrencyRatesUseCase
) : ViewModel() {

    val state: StateFlow<BankAccountState> = getBankAccountListUseCase()
        .map {
            BankAccountState(
                displayState = BankAccountDisplayState.Content(
                    accounts = it,
                    totalBalance = calculateTotalBalance(it)
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = BankAccountState(displayState = BankAccountDisplayState.Loading)
        )

    private suspend fun calculateTotalBalance(accounts: List<BankAccountEntity>): Double {
        val currencyRates = getCurrencyRatesUseCase.invoke()
        var totalBalance = 0.0
        for (account in accounts) {
            totalBalance += if (account.currency == Currency.RUB) {
                account.balance.toDouble()
            } else {
                currencyRates[account.currency.name]?.times(account.balance.toDouble())
                    ?: throw RuntimeException()
            }
        }
        return totalBalance
    }

}