package com.robotbot.financetracker.presentation.bank_account.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotbot.financetracker.domain.entities.TransferEntity
import com.robotbot.financetracker.domain.usecases.account.GetBankAccountUseCase
import com.robotbot.financetracker.domain.usecases.transfer.AddTransferUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

class CreateTransferViewModel @Inject constructor(
    private val getBankAccountUseCase: GetBankAccountUseCase,
    private val addTransferUseCase: AddTransferUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CreateTransferState())
    val state = _state.asStateFlow()

    private val amount = MutableStateFlow("")

    init {
        checkSaveButtonIsAvailable()
    }

    private fun checkSaveButtonIsAvailable() {
        combine(
            state.map { it.accountFrom != null && it.accountTo != null },
            amount.map { it.isValidAmount() }
        ) { accountsSet, isAmountValid ->
            accountsSet && isAmountValid
        }.onEach { isEnabled ->
            _state.update { it.copy(saveButtonEnabled = isEnabled) }
        }.launchIn(viewModelScope)
    }

    private fun String.isValidAmount(): Boolean {
        val amount = this.trim()
        return amount.isNotBlank() && amount.toBigDecimalOrNull()?.let { it > BigDecimal.ZERO } == true
    }

    fun saveTransfer(amountInput: String) {
        val currentState = state.value
        val accountFrom = currentState.accountFrom
        val accountTo = currentState.accountTo
        if (accountFrom == null || accountTo == null || !amountInput.isValidAmount()) {
            _state.update {
                it.copy(displayState = CreateTransferDisplayState.Error)
            }
            return
        }
        val amount = amountInput.trim().toBigDecimal()
        viewModelScope.launch {
            val transfer = TransferEntity(
                accountFrom = accountFrom,
                accountTo = accountTo,
                amount = amount
            )
            addTransferUseCase(transfer)
            _state.update {
                it.copy(displayState = CreateTransferDisplayState.WorkEnded)
            }
        }
    }

    fun setAmount(amountInput: String) {
        amount.value = amountInput
    }

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
                    currentState.copy(
                        accountFrom = bankAccount,
                        accountTo = if (currentState.accountTo == bankAccount) null else currentState.accountTo
                    )
                } else {
                    currentState.copy(
                        accountFrom = if (currentState.accountFrom == bankAccount) null else currentState.accountFrom,
                        accountTo = bankAccount
                    )
                }
            }
        }
    }
}