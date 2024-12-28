package com.robotbot.financetracker.domain.usecases.account

import com.robotbot.financetracker.domain.repotisories.BankAccountRepository
import javax.inject.Inject

class DeleteBankAccountUseCase @Inject constructor(
    private val repository: BankAccountRepository
) {

    suspend operator fun invoke(id: Int) = repository.delete(id)

}