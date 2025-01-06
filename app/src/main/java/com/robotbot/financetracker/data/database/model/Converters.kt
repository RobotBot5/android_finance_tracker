package com.robotbot.financetracker.data.database.model

import androidx.room.TypeConverter
import com.robotbot.financetracker.domain.entities.TransactionType
import kotlin.enumValueOf

class Converters {

    @TypeConverter
    fun toTransactionType(value: String) = enumValueOf<TransactionType>(value)

    @TypeConverter
    fun fromTransactionType(value: TransactionType) = value.name

}