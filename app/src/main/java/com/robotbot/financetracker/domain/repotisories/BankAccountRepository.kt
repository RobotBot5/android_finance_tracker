package com.robotbot.financetracker.domain.repotisories

import com.robotbot.financetracker.domain.entities.BankAccountEntity
import java.math.BigDecimal

interface BankAccountRepository : CrudRepository<BankAccountEntity> {

    suspend fun updateBalance(accountId: Int, newBalance: BigDecimal)
}