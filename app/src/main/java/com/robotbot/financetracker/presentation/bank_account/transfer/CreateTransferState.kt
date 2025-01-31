package com.robotbot.financetracker.presentation.bank_account.transfer

import android.util.Log
import com.robotbot.financetracker.domain.entities.BankAccountEntity
import java.math.BigDecimal
import java.util.Calendar

data class CreateTransferState(

    val currencyState: CreateTransferCurrencyState = CreateTransferCurrencyState.SingleCurrency,
    val displayState: CreateTransferDisplayState = CreateTransferDisplayState.Content,
    val accountFrom: BankAccountEntity? = null,
    val accountTo: BankAccountEntity? = null,
    val amountFrom: BigDecimal? = null,
    val selectedDate: Calendar
) {
    val saveButtonEnabled: Boolean
        get() {
            Log.d("CreateTransferState", currencyState.toString())
            return accountFrom != null && accountTo != null && amountFrom != null &&
                    (currencyState !is CreateTransferCurrencyState.DifferentCurrencies ||
                            (currencyState.amountTo != null || currencyState.amountToPlaceholder != BigDecimal.ZERO))
        }

}

sealed interface CreateTransferDisplayState {

    data object Loading : CreateTransferDisplayState

    data object Content : CreateTransferDisplayState

    data class Error(val errorState: ErrorState) : CreateTransferDisplayState

    data object WorkEnded : CreateTransferDisplayState
}

sealed interface CreateTransferCurrencyState {

    data object SingleCurrency : CreateTransferCurrencyState

    data class DifferentCurrencies(
        val amountToPlaceholder: BigDecimal,
        val amountTo: BigDecimal? = null
    ) : CreateTransferCurrencyState
}

sealed interface ErrorState {

    data object UnknownError : ErrorState

    data object InsufficientFunds : ErrorState

    data object InvalidTransfer : ErrorState
}