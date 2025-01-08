package com.robotbot.financetracker.domain.usecases.account

import com.robotbot.financetracker.di.RealBankAccountDatabaseQualifier
import com.robotbot.financetracker.domain.repotisories.BankAccountRepository
import javax.inject.Inject

class GetBankAccountListUseCase @Inject constructor(
    @RealBankAccountDatabaseQualifier private val repository: BankAccountRepository
) {

    operator fun invoke() = repository.getAll()

}