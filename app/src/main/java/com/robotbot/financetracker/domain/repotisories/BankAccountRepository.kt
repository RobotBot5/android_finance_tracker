package com.robotbot.financetracker.domain.repotisories

import com.robotbot.financetracker.domain.entities.BankAccountEntity

interface BankAccountRepository : CrudRepository<BankAccountEntity> {

    suspend fun getPrices(): Map<String, String>

}