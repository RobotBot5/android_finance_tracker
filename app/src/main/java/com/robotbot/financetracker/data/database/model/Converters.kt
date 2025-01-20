package com.robotbot.financetracker.data.database.model

import androidx.room.TypeConverter
import com.robotbot.financetracker.domain.entities.Currency
import com.robotbot.financetracker.domain.entities.TransactionType
import java.math.BigDecimal
import java.util.Calendar
import kotlin.enumValueOf

class Converters {

    @TypeConverter
    fun toTransactionType(value: String) = enumValueOf<TransactionType>(value)

    @TypeConverter
    fun fromTransactionType(value: TransactionType) = value.name

    @TypeConverter
    fun toCurrency(value: String) = enumValueOf<Currency>(value)

    @TypeConverter
    fun fromCurrency(value: Currency) = value.name

    @TypeConverter
    fun bigDecimalToString(value: BigDecimal): String = value.toPlainString()

    @TypeConverter
    fun stringToBigDecimal(value: String): BigDecimal = value.toBigDecimal()

    @TypeConverter
    fun fromTimestamp(value: Long): Calendar? =
        Calendar.getInstance().apply { timeInMillis = value }

    @TypeConverter
    fun dateToTimestamp(calendar: Calendar): Long = calendar.timeInMillis
}
