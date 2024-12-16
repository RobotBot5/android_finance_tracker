package com.robotbot.financetracker.domain.usecases.account

import com.robotbot.financetracker.domain.entities.BankAccountEntity
import com.robotbot.financetracker.domain.repotisories.BankAccountRepository
import javax.inject.Inject

class AddBankAccountUseCase @Inject constructor(
    private val repository: BankAccountRepository
) {

    operator fun invoke(account: BankAccountEntity) = repository.create(account)

}