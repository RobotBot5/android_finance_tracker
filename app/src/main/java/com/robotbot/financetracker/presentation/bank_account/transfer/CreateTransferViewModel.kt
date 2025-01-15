package com.robotbot.financetracker.presentation.bank_account.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotbot.financetracker.domain.entities.BankAccountEntity
import com.robotbot.financetracker.domain.usecases.account.GetBankAccountListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateTransferViewModel @Inject constructor(
    private val getBankAccountListUseCase: GetBankAccountListUseCase
) : ViewModel() {

    lateinit var bankAccounts: MutableList<BankAccountEntity>

    private val _state = MutableStateFlow(CreateTransferState())
    val state = _state.asStateFlow()

    init {
        _state.update {
            it.copy(displayState = CreateTransferDisplayState.Loading)
        }
        viewModelScope.launch {
            bankAccounts = getBankAccountListUseCase().first().toMutableList()
            _state.update {
                it.copy(displayState = CreateTransferDisplayState.Loaded)
            }
        }
    }
}