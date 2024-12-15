package com.robotbot.financetracker.domain.usecases.transaction

import com.robotbot.financetracker.domain.entities.BankAccountEntity
import com.robotbot.financetracker.domain.entities.TransactionEntity
import com.robotbot.financetracker.domain.repotisories.BankAccountRepository
import com.robotbot.financetracker.domain.repotisories.TransactionRepository

class GetTransactionListUseCase(
    private val repository: TransactionRepository
) {

    operator fun invoke() = repository.getAll()

}