package com.robotbot.financetracker.presentation

import com.robotbot.financetracker.domain.entities.Currency

data class CreateAccountState(
    val nameError: String? = null,
    val balanceError: String? = null,
    val selectedCurrency: Currency = Currency.RUB,
    val isAccountCreated: Boolean = false
)