package com.robotbot.financetracker.domain.usecases.transfer

import com.robotbot.financetracker.domain.entities.BankAccountEntity
import com.robotbot.financetracker.domain.entities.TransferEntity
import com.robotbot.financetracker.domain.repotisories.TransferRepository
import com.robotbot.financetracker.domain.usecases.account.GetBankAccountUseCase
import java.math.BigDecimal
import java.util.Calendar
import javax.inject.Inject

class EditTransferUseCase @Inject constructor(
    private val deleteTransferUseCase: DeleteTransferUseCase,
    private val checkValidTransferUseCase: CheckValidTransferUseCase,
    private val updateBalancesUseCase: UpdateBalancesUseCase,
    private val transferRepository: TransferRepository,
    private val getBankAccountUseCase: GetBankAccountUseCase
) {

    suspend operator fun invoke(
        oldTransferEntity: TransferEntity,
        accountFrom: BankAccountEntity,
        accountTo: BankAccountEntity,
        amountFrom: BigDecimal,
        amountTo: BigDecimal,
        date: Calendar
    ): Result {

        deleteTransferUseCase(oldTransferEntity).let {
            if (it !is Result.Success) return it
        }

        val newAccountFrom = getBankAccountUseCase(accountFrom.id)
        val newAccountTo = getBankAccountUseCase(accountTo.id)

        if (!checkValidTransferUseCase(
                accountFrom = newAccountFrom,
                accountTo = newAccountTo,
                amountFrom = amountFrom,
                amountTo = amountTo
            )
        ) {
            return Result.InvalidTransfer
        }

        updateBalancesUseCase(
            accountFromId = accountFrom.id,
            accountToId = accountTo.id,
            newBalanceFrom = newAccountFrom.balance - amountFrom,
            newBalanceTo = newAccountTo.balance + amountTo
        ).let {
            if (it !is Result.Success) return it
        }

        val transfer = TransferEntity(
            id = oldTransferEntity.id,
            accountFrom = accountFrom,
            accountTo = accountTo,
            amountFrom = amountFrom,
            amountTo = amountTo,
            date = date
        )

        return try {
            transferRepository.update(transfer)
            Result.Success
        } catch (e: Exception) {
            Result.UnknownError(e)
        }
    }
}