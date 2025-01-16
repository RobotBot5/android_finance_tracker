package com.robotbot.financetracker.domain.entities

import com.robotbot.financetracker.domain.DomainConstants.UNDEFINED_ID
import java.math.BigDecimal
import java.time.LocalDateTime

data class TransferEntity(
    val accountFrom: BankAccountEntity,
    val accountTo: BankAccountEntity,
    val amount: BigDecimal,
//    val date: LocalDateTime,
    var id: Int = UNDEFINED_ID
)
