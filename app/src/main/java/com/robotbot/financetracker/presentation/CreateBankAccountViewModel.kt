package com.robotbot.financetracker.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotbot.financetracker.R
import com.robotbot.financetracker.domain.entities.BankAccountEntity
import com.robotbot.financetracker.domain.entities.Currency
import com.robotbot.financetracker.domain.usecases.account.AddBankAccountUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

class CreateBankAccountViewModel @Inject constructor(
    private val addBankAccountUseCase: AddBankAccountUseCase,
    private val application: Application
) : ViewModel() {

    private val _state = MutableStateFlow(CreateAccountState())
    val state = _state.asStateFlow()

    fun addAccount(inputName: String, inputBalance: String) {
        val name = parseName(inputName)
        val balance = parseBalance(inputBalance)

        val nameError = validateName(name)
        val balanceError = validateBalance(balance)

        if (nameError != null || balanceError != null) {
            _state.value = _state.value.copy(
                nameError = nameError,
                balanceError = balanceError
            )
            return
        }
        viewModelScope.launch {
            addBankAccountUseCase(
                BankAccountEntity(
                    name = name,
                    balance = balance,
                    currency = _state.value.selectedCurrency
                )
            )
            _state.value = _state.value.copy(isAccountCreated = true)
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
        _state.value = _state.value.copy(
            nameError = null
        )
    }

    fun resetErrorInputBalance() {
        _state.value = _state.value.copy(
            balanceError = null
        )
    }

    fun setCurrency(currency: Currency) {
        _state.value = _state.value.copy(
            selectedCurrency = currency
        )
    }

    companion object {

        private val ERROR_PARSE_BALANCE = BigDecimal.ZERO

    }

}