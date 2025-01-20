package com.robotbot.financetracker.domain.usecases.transfer

import com.robotbot.financetracker.di.RealBankAccountDatabaseQualifier
import com.robotbot.financetracker.domain.entities.BankAccountEntity
import com.robotbot.financetracker.domain.entities.TransferEntity
import com.robotbot.financetracker.domain.repotisories.BankAccountRepository
import com.robotbot.financetracker.domain.repotisories.TransferRepository
import java.math.BigDecimal
import java.util.Calendar
import javax.inject.Inject

class AddTransferUseCase @Inject constructor(
    private val transferRepository: TransferRepository,
    @RealBankAccountDatabaseQualifier private val bankAccountRepository: BankAccountRepository
) {

    suspend operator fun invoke(
        accountFrom: BankAccountEntity,
        accountTo: BankAccountEntity,
        amountFrom: BigDecimal,
        amountTo: BigDecimal,
        date: Calendar
    ): Result {

        if (!isValidTransfer(accountFrom, accountTo, amountFrom, amountTo)) {
            return Result.InvalidTransfer
        }

        if (accountFrom.balance < amountFrom) {
            return Result.InsufficientFunds
        }

        val newBalanceFrom = accountFrom.balance - amountFrom
        val newBalanceTo = accountTo.balance + amountTo

        val transfer = TransferEntity(
            accountFrom = accountFrom,
            accountTo = accountTo,
            amountFrom = amountFrom,
            amountTo = amountTo,
            date = date
        )

        return try {
            bankAccountRepository.updateBalance(
                accountId = accountFrom.id,
                newBalance =  newBalanceFrom
            )
            bankAccountRepository.updateBalance(
                accountId = accountTo.id,
                newBalance =  newBalanceTo
            )
            transferRepository.create(transfer)
            Result.Success
        } catch (e: Exception) {
            Result.UnknownError(e)
        }
    }

    private fun isValidTransfer(
        accountFrom: BankAccountEntity,
        accountTo: BankAccountEntity,
        amountFrom: BigDecimal,
        amountTo: BigDecimal
    ): Boolean {
        return accountFrom != accountTo && amountFrom > BigDecimal.ZERO && amountTo > BigDecimal.ZERO
    }

    sealed interface Result {
        data object Success : Result
        data object InvalidTransfer : Result
        data object InsufficientFunds : Result
        data class UnknownError(val exception: Exception) : Result
    }
}