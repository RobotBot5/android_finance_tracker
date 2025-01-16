package com.robotbot.financetracker.presentation.bank_account.transfer.choose_account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotbot.financetracker.domain.usecases.account.GetBankAccountListUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class ChooseAccountViewModel @Inject constructor(
    getBankAccountListUseCase: GetBankAccountListUseCase
) : ViewModel() {

    val state = getBankAccountListUseCase()
        .map { ChooseAccountState.Loaded(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = ChooseAccountState.Loading
        )

}