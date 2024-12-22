package com.robotbot.financetracker.domain.usecases.transaction

import com.robotbot.financetracker.domain.entities.BankAccountEntity
import com.robotbot.financetracker.domain.entities.TransactionEntity
import com.robotbot.financetracker.domain.repotisories.BankAccountRepository
import com.robotbot.financetracker.domain.repotisories.TransactionRepository

class GetTransactionUseCase(
    private val repository: TransactionRepository
) {

    suspend operator fun invoke(id: Int) = repository.getById(id)

}