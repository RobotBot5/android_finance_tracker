package com.robotbot.financetracker.domain.usecases.account

import com.robotbot.financetracker.domain.repotisories.BankAccountRepository
import javax.inject.Inject

class GetBankAccountListUseCase @Inject constructor(
    private val repository: BankAccountRepository
) {

    operator fun invoke() = repository.getAll()

}