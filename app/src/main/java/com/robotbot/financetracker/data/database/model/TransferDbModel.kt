package com.robotbot.financetracker.data.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.robotbot.financetracker.domain.entities.BankAccountEntity
import java.math.BigDecimal

@Entity(
    tableName = "transfers",
    foreignKeys = [
        ForeignKey(
            entity = BankAccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["fromAccountId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BankAccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["toAccountId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("fromAccountId"), Index("toAccountId")]
)
data class TransferDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val fromAccountId: Int,
    val toAccountId: Int,
    val amount: BigDecimal,
//    val date: LocalDateTime,
)