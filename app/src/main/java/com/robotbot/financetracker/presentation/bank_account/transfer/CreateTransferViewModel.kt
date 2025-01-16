package com.robotbot.financetracker.presentation.bank_account.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotbot.financetracker.domain.usecases.account.GetBankAccountUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateTransferViewModel @Inject constructor(
    private val getBankAccountUseCase: GetBankAccountUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CreateTransferState())
    val state = _state.asStateFlow()

    fun setFromAccount(bankAccountId: Int) {
        setAccount(bankAccountId = bankAccountId, isFromAccount = true)
    }

    fun setToAccount(bankAccountId: Int) {
        setAccount(bankAccountId = bankAccountId, isFromAccount = false)
    }

    private fun setAccount(
        bankAccountId: Int,
        isFromAccount: Boolean
    ) {
        viewModelScope.launch {
            val bankAccount = getBankAccountUseCase(bankAccountId)
            _state.update { currentState ->
                if (isFromAccount) {
                    if (currentState.accountTo == bankAccount) {
                        currentState.copy(
                            accountFrom = bankAccount,
                            accountTo = null
                        )
                    } else {
                        currentState.copy(accountFrom = bankAccount)
                    }
                } else {
                    if (currentState.accountFrom == bankAccount) {
                        currentState.copy(
                            accountFrom = null,
                            accountTo = bankAccount
                        )
                    } else {
                        currentState.copy(accountTo = bankAccount)
                    }
                }
            }
        }
    }
}