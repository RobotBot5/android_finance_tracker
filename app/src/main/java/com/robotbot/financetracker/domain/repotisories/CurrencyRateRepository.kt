package com.robotbot.financetracker.domain.repotisories

import com.robotbot.financetracker.domain.entities.Currency
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface CurrencyRateRepository {

    //TODO REPLACE THIS METHOD WITH NEW realGetCurrencyRates()
    suspend fun getCurrencyRates(): Map<String, Double>

    fun realGetCurrencyRates(
        mainCurrency: Currency,
        otherCurrencies: List<Currency>
    ): Flow<Map<Currency, BigDecimal>>

}