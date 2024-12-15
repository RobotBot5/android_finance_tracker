package com.robotbot.financetracker.domain.usecases.account

import com.robotbot.financetracker.domain.repotisories.BankAccountRepository

class GetBankAccountListUseCase(
    private val repository: BankAccountRepository
) {

    operator fun invoke() = repository.getAll()

}