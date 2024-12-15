package com.robotbot.financetracker.domain.usecases.transfer

import com.robotbot.financetracker.domain.repotisories.TransferRepository

class GetTransferListUseCase(
    private val repository: TransferRepository
) {

    operator fun invoke() = repository.getAll()

}