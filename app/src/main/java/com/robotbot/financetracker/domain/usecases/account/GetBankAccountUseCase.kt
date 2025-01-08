package com.robotbot.financetracker.domain.usecases.account

import com.robotbot.financetracker.di.RealBankAccountDatabaseQualifier
import com.robotbot.financetracker.domain.repotisories.BankAccountRepository
import javax.inject.Inject

class GetBankAccountUseCase @Inject constructor(
    @RealBankAccountDatabaseQualifier private val repository: BankAccountRepository
) {

    suspend operator fun invoke(id: Int) = repository.getById(id)

}