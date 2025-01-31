package com.robotbot.financetracker.domain.usecases.transfer

import com.robotbot.financetracker.domain.entities.TransferEntity
import com.robotbot.financetracker.domain.repotisories.TransferRepository
import javax.inject.Inject

class DeleteTransferUseCase @Inject constructor(
    private val repository: TransferRepository,
    private val updateBalancesUseCase: UpdateBalancesUseCase
) {

    suspend operator fun invoke(transferEntity: TransferEntity): Result {
        updateBalancesUseCase(
            accountFromId = transferEntity.accountFrom.id,
            accountToId = transferEntity.accountTo.id,
            newBalanceFrom = transferEntity.accountFrom.balance + transferEntity.amountFrom,
            newBalanceTo = transferEntity.accountTo.balance - transferEntity.amountTo
        ).let {
            if (it !is Result.Success) return it
        }

        return try {
            repository.delete(transferEntity.id)
            Result.Success
        } catch (e: Exception) {
            Result.UnknownError(e)
        }
    }

}