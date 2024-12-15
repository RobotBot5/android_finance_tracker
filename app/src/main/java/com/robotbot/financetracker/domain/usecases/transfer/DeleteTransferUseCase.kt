package com.robotbot.financetracker.domain.usecases.transfer

import com.robotbot.financetracker.domain.repotisories.TransferRepository

class DeleteTransferUseCase(
    private val repository: TransferRepository
) {

    operator fun invoke(id: Int) = repository.delete(id)

}