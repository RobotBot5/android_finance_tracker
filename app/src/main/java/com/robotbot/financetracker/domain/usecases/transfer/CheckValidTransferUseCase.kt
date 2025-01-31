package com.robotbot.financetracker.domain.usecases.transfer

import com.robotbot.financetracker.domain.entities.BankAccountEntity
import java.math.BigDecimal
import javax.inject.Inject

class CheckValidTransferUseCase @Inject constructor() {

    operator fun invoke(
        accountFrom: BankAccountEntity,
        accountTo: BankAccountEntity,
        amountFrom: BigDecimal,
        amountTo: BigDecimal
    ): Boolean =
        accountFrom != accountTo && amountFrom > BigDecimal.ZERO && amountTo > BigDecimal.ZERO
}