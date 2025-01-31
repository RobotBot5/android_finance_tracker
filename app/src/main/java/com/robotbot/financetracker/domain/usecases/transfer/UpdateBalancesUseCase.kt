package com.robotbot.financetracker.domain.usecases.transfer

import com.robotbot.financetracker.di.RealBankAccountDatabaseQualifier
import com.robotbot.financetracker.domain.repotisories.BankAccountRepository
import java.math.BigDecimal
import javax.inject.Inject

class UpdateBalancesUseCase @Inject constructor(
    @RealBankAccountDatabaseQualifier private val bankAccountRepository: BankAccountRepository
) {

    suspend operator fun invoke(
        accountFromId: Int,
        accountToId: Int,
        newBalanceFrom: BigDecimal,
        newBalanceTo: BigDecimal,
    ): Result {

        return try {
            bankAccountRepository.updateBalance(
                accountId = accountFromId,
                newBalance =  newBalanceFrom
            )
            bankAccountRepository.updateBalance(
                accountId = accountToId,
                newBalance =  newBalanceTo
            )
            Result.Success
        } catch (e: Exception) {
            Result.UnknownError(e)
        }
    }
}