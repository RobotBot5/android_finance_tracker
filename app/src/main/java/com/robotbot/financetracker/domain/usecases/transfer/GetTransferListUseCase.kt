package com.robotbot.financetracker.domain.usecases.transfer

import com.robotbot.financetracker.domain.repotisories.TransferRepository
import javax.inject.Inject

class GetTransferListUseCase @Inject constructor(
    private val repository: TransferRepository
) {

    operator fun invoke() = repository.getAll()

}