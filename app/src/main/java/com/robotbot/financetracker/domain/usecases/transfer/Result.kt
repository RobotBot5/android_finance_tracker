package com.robotbot.financetracker.domain.usecases.transfer

sealed interface Result {
    data object Success : Result
    data object InvalidTransfer : Result
    data class UnknownError(val exception: Exception) : Result
}