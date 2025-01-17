package com.robotbot.financetracker.domain.usecases.transfer

import com.robotbot.financetracker.di.RealBankAccountDatabaseQualifier
import com.robotbot.financetracker.domain.entities.BankAccountEntity
import com.robotbot.financetracker.domain.entities.TransferEntity
import com.robotbot.financetracker.domain.repotisories.BankAccountRepository
import com.robotbot.financetracker.domain.repotisories.TransferRepository
import java.math.BigDecimal
import javax.inject.Inject

class AddTransferUseCase @Inject constructor(
    private val transferRepository: TransferRepository,
    @RealBankAccountDatabaseQualifier private val bankAccountRepository: BankAccountRepository
) {

    suspend operator fun invoke(
        accountFrom: BankAccountEntity,
        accountTo: BankAccountEntity,
        amount: BigDecimal
    ): Result {

        if (!isValidTransfer(accountFrom, accountTo, amount)) {
            return Result.InvalidTransfer
        }

        if (accountFrom.balance < amount) {
            return Result.InsufficientFunds
        }

        val newBalanceFrom = accountFrom.balance - amount
        val newBalanceTo = accountTo.balance - amount

        val transfer = TransferEntity(
            accountFrom = accountFrom,
            accountTo = accountTo,
            amount = amount
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
        amount: BigDecimal
    ): Boolean {
        return accountFrom != accountTo && amount > BigDecimal.ZERO
    }

    sealed interface Result {
        data object Success : Result
        data object InvalidTransfer : Result
        data object InsufficientFunds : Result
        data class UnknownError(val exception: Exception) : Result
    }
}