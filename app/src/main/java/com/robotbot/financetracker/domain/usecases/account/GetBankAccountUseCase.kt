package com.robotbot.financetracker.domain.usecases.account

import com.robotbot.financetracker.domain.repotisories.BankAccountRepository

class GetBankAccountUseCase(
    private val repository: BankAccountRepository
) {

    operator fun invoke(id: Int) = repository.getById(id)

}