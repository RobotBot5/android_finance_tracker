package com.robotbot.financetracker.domain.usecases.account

import com.robotbot.financetracker.di.RealBankAccountDatabaseQualifier
import com.robotbot.financetracker.domain.entities.BankAccountEntity
import com.robotbot.financetracker.domain.repotisories.BankAccountRepository
import javax.inject.Inject

class AddBankAccountUseCase @Inject constructor(
    @RealBankAccountDatabaseQualifier private val repository: BankAccountRepository
) {

    suspend operator fun invoke(account: BankAccountEntity) = repository.create(account)

}