package com.robotbot.financetracker.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotbot.financetracker.R
import com.robotbot.financetracker.domain.DomainConstants
import com.robotbot.financetracker.domain.entities.BankAccountEntity
import com.robotbot.financetracker.domain.entities.Currency
import com.robotbot.financetracker.domain.usecases.account.AddBankAccountUseCase
import com.robotbot.financetracker.domain.usecases.account.DeleteBankAccountUseCase
import com.robotbot.financetracker.domain.usecases.account.EditBankAccountUseCase
import com.robotbot.financetracker.domain.usecases.account.GetBankAccountUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

class ManageBankAccountViewModel @Inject constructor(
    private val addBankAccountUseCase: AddBankAccountUseCase,
    private val getBankAccountUseCase: GetBankAccountUseCase,
    private val editBankAccountUseCase: EditBankAccountUseCase,
    private val deleteBankAccountUseCase: DeleteBankAccountUseCase,
    private val application: Application
) : ViewModel() {

    private val _state = MutableStateFlow(AccountManagementState())
    val state = _state.asStateFlow()

    private var editAccountId: Int = DomainConstants.UNDEFINED_ID

    fun deleteAccount() {
        viewModelScope.launch {
            deleteBankAccountUseCase(editAccountId)
            _state.update {
                it.copy(
                    displayState = DisplayState.WorkEnded
                )
            }
        }
    }

    fun editAccount(inputName: String, inputBalance: String) {
        handleAccountOperation(inputName, inputBalance) { account ->
            editBankAccountUseCase(account)
        }
    }

    fun addAccount(inputName: String, inputBalance: String) {
        handleAccountOperation(inputName, inputBalance) { account ->
            addBankAccountUseCase(account)
        }
    }

    private fun handleAccountOperation(
        inputName: String,
        inputBalance: String,
        operation: suspend (BankAccountEntity) -> Unit
    ) {
        val name = parseName(inputName)
        val balance = parseBalance(inputBalance)

        val nameError = validateName(name)
        val balanceError = validateBalance(balance)

        if (nameError != null || balanceError != null) {
            _state.update {
                it.copy(
                    displayState = DisplayState.Content(
                        nameError = nameError,
                        balanceError = balanceError
                    )
                )
            }
            return
        }

        viewModelScope.launch {
            operation(
                BankAccountEntity(
                    id = editAccountId,
                    name = name,
                    balance = balance,
                    currency = _state.value.selectedCurrency
                )
            )
            _state.update {
                it.copy(displayState = DisplayState.WorkEnded)
            }
        }
    }

    private fun updateErrorStateIfContent(update: DisplayState.Content.() -> DisplayState.Content) {
        val currentDisplayState = _state.value.displayState
        if (currentDisplayState is DisplayState.Content) {
            _state.update {
                it.copy(displayState = currentDisplayState.update())
            }
        } else {
            _state.update {
                it.copy(displayState = DisplayState.Content().update())
            }
        }
    }

    private fun validateName(name: String): String? = if (name.isBlank())
        application.getString(R.string.error_incorrect_name)
    else
        null

    private fun validateBalance(balance: BigDecimal): String? = if (balance <= BigDecimal.ZERO)
        application.getString(R.string.error_incorrect_balance)
    else
        null


    private fun parseName(name: String): String {
        return name.trim()
    }

    private fun parseBalance(balance: String): BigDecimal {
        val parsedStringBalance = balance.trim()
        return try {
            parsedStringBalance.toBigDecimal()
        } catch (e: NumberFormatException) {
            ERROR_PARSE_BALANCE
        }
    }

    fun resetErrorInputName() {
        updateErrorStateIfContent {
            copy(nameError = null)
        }
    }

    fun resetErrorInputBalance() {
        updateErrorStateIfContent {
            copy(balanceError = null)
        }
    }

    fun setCurrency(currency: Currency) {
        _state.value = _state.value.copy(
            selectedCurrency = currency
        )
    }

    fun loadAccountEntityById(accountId: Int) {
        viewModelScope.launch {
            val accountEntity = getBankAccountUseCase(accountId)
            editAccountId = accountEntity.id
            _state.update {
                it.copy(
                    displayState = DisplayState.InitialEditMode(
                        accountEntity
                    ),
                    selectedCurrency = accountEntity.currency
                )
            }
        }
    }

    companion object {

        private val ERROR_PARSE_BALANCE = BigDecimal.ZERO

    }

}