package com.robotbot.financetracker.data.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class TransferWithAccountsDbModel(
    @Embedded val transfer: TransferDbModel,
    @Relation(
        parentColumn = "fromAccountId",
        entityColumn = "id"
    )
    val fromBankAccount: BankAccountDbModel,
    @Relation(
        parentColumn = "toAccountId",
        entityColumn = "id"
    )
    val toBankAccount: BankAccountDbModel
)
