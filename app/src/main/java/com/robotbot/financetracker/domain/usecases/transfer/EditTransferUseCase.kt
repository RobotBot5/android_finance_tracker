package com.robotbot.financetracker.domain.usecases.transfer

import com.robotbot.financetracker.domain.entities.TransferEntity
import com.robotbot.financetracker.domain.repotisories.TransferRepository

class EditTransferUseCase(
    private val repository: TransferRepository
) {

    suspend operator fun invoke(transfer: TransferEntity) = repository.update(transfer)

}