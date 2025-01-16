package com.robotbot.financetracker.domain.usecases.transfer

import com.robotbot.financetracker.domain.entities.TransferEntity
import com.robotbot.financetracker.domain.repotisories.TransferRepository
import javax.inject.Inject

class EditTransferUseCase @Inject constructor(
    private val repository: TransferRepository
) {

    suspend operator fun invoke(transfer: TransferEntity) = repository.update(transfer)

}