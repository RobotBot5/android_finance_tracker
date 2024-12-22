package com.robotbot.financetracker.domain.usecases.transfer

import com.robotbot.financetracker.domain.repotisories.TransferRepository

class GetTransferUseCase(
    private val repository: TransferRepository
) {

    suspend operator fun invoke(id: Int) = repository.getById(id)

}