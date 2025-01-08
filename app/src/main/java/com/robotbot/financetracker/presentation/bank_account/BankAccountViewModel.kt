package com.robotbot.financetracker.presentation.bank_account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotbot.financetracker.domain.usecases.account.GetBankAccountListUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class BankAccountViewModel @Inject constructor(
    getBankAccountListUseCase: GetBankAccountListUseCase
) : ViewModel() {

    val state: StateFlow<BankAccountState> = getBankAccountListUseCase()
        .map { BankAccountState(displayState = BankAccountDisplayState.Content(it)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = BankAccountState(displayState = BankAccountDisplayState.Loading)
        )

}