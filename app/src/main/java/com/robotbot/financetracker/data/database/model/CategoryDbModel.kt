package com.robotbot.financetracker.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.robotbot.financetracker.domain.entities.TransactionType

@Entity(tableName = "categories")
data class CategoryDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val transactionType: TransactionType,
    val name: String,
    val iconResName: String,
)
