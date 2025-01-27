package com.robotbot.financetracker.presentation.bank_account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotbot.financetracker.domain.usecases.transfer.GetTransferListUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class TransferHistoryViewModel @Inject constructor(
    getTransferListUseCase: GetTransferListUseCase
) : ViewModel() {

    val state = getTransferListUseCase()
        .map { TransferHistoryState.Content(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = TransferHistoryState.Loading
        )
}