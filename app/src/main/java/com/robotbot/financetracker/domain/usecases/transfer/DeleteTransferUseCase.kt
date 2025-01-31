package com.robotbot.financetracker.domain.usecases.transfer

import com.robotbot.financetracker.di.RealBankAccountDatabaseQualifier
import com.robotbot.financetracker.domain.entities.TransferEntity
import com.robotbot.financetracker.domain.repotisories.BankAccountRepository
import com.robotbot.financetracker.domain.repotisories.TransferRepository
import javax.inject.Inject

class DeleteTransferUseCase @Inject constructor(
    private val repository: TransferRepository,
    @RealBankAccountDatabaseQualifier private val bankAccountRepository: BankAccountRepository
) {

    suspend operator fun invoke(transferEntity: TransferEntity) {
        updateBalances(transferEntity)
        repository.delete(transferEntity.id)
    }

    private suspend fun updateBalances(transferEntity: TransferEntity) {
        val accountFrom = transferEntity.accountFrom
        val newBalanceAccountFrom = accountFrom.balance + transferEntity.amountFrom
        bankAccountRepository.updateBalance(
            accountId = accountFrom.id,
            newBalance = newBalanceAccountFrom
        )
        val accountTo = transferEntity.accountTo
        val newBalanceAccountTo = accountTo.balance - transferEntity.amountTo
        bankAccountRepository.updateBalance(
            accountId = accountTo.id,
            newBalance = newBalanceAccountTo
        )
    }

}