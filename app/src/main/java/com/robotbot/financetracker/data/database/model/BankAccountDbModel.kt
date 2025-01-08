package com.robotbot.financetracker.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.robotbot.financetracker.domain.entities.Currency
import java.math.BigDecimal

@Entity(tableName = "bank_accounts")
data class BankAccountDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val balance: BigDecimal,
    val currency: Currency
)
