package com.robotbot.financetracker.domain.usecases.account

import com.robotbot.financetracker.domain.repotisories.BankAccountRepository

class DeleteBankAccountUseCase(
    private val repository: BankAccountRepository
) {

    operator fun invoke(id: Int) = repository.delete(id)

}