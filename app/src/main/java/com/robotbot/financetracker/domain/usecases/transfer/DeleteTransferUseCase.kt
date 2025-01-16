package com.robotbot.financetracker.domain.usecases.transfer

import com.robotbot.financetracker.domain.repotisories.TransferRepository
import javax.inject.Inject

class DeleteTransferUseCase @Inject constructor(
    private val repository: TransferRepository
) {

    suspend operator fun invoke(id: Int) = repository.delete(id)

}