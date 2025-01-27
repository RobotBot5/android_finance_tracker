package com.robotbot.financetracker.presentation.bank_account

import com.robotbot.financetracker.domain.entities.TransferEntity

sealed interface TransferHistoryState {

    data object Initial : TransferHistoryState
    data object Loading : TransferHistoryState
    data object Error : TransferHistoryState
    data class Content(val transfers: List<TransferEntity>) : TransferHistoryState
}