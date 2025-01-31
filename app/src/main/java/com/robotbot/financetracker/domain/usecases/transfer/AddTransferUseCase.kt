package com.robotbot.financetracker.domain.usecases.transfer

import com.robotbot.financetracker.domain.entities.BankAccountEntity
import com.robotbot.financetracker.domain.entities.TransferEntity
import com.robotbot.financetracker.domain.repotisories.TransferRepository
import java.math.BigDecimal
import java.util.Calendar
import javax.inject.Inject

class AddTransferUseCase @Inject constructor(
    private val transferRepository: TransferRepository,
    private val checkValidTransferUseCase: CheckValidTransferUseCase,
    private val updateBalancesUseCase: UpdateBalancesUseCase
) {

    suspend operator fun invoke(
        accountFrom: BankAccountEntity,
        accountTo: BankAccountEntity,
        amountFrom: BigDecimal,
        amountTo: BigDecimal,
        date: Calendar
    ): Result {

        if (!checkValidTransferUseCase(
                accountFrom = accountFrom,
                accountTo = accountTo,
                amountFrom = amountFrom,
                amountTo = amountTo
            )
        ) {
            return Result.InvalidTransfer
        }

        updateBalancesUseCase(
            accountFromId = accountFrom.id,
            accountToId = accountTo.id,
            newBalanceFrom = accountFrom.balance - amountFrom,
            newBalanceTo = accountTo.balance + amountTo
        ).let {
            if (it !is Result.Success) return it
        }

        val transfer = TransferEntity(
            accountFrom = accountFrom,
            accountTo = accountTo,
            amountFrom = amountFrom,
            amountTo = amountTo,
            date = date
        )

        return try {
            transferRepository.create(transfer)
            Result.Success
        } catch (e: Exception) {
            Result.UnknownError(e)
        }
    }
}