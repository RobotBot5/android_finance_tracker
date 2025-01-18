package com.robotbot.financetracker.data.mapper

import com.google.gson.JsonObject
import com.robotbot.financetracker.domain.entities.Currency
import java.math.BigDecimal
import javax.inject.Inject

class CurrencyRateMapper @Inject constructor() {

    fun mapApiResponseToMap(jsonObject: JsonObject): Map<Currency, BigDecimal> {
        val currenciesSet = jsonObject.keySet()
        val currenciesMap = mutableMapOf<Currency, BigDecimal>()
        for (currency in currenciesSet) {
            val currencyRate = jsonObject.getAsJsonPrimitive(currency).asBigDecimal
            currenciesMap[Currency.valueOf(currency)] = currencyRate
        }
        return currenciesMap
    }

    fun mapMainCurrencyForQuery(currency: Currency): String = currency.name

    fun mapOtherCurrencyForQuery(currencies: List<Currency>): String =
        currencies.joinToString(",") { it.name }
}