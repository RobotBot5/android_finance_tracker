package com.robotbot.financetracker.domain.repotisories

import com.robotbot.financetracker.domain.entities.Currency
import java.math.BigDecimal

interface CurrencyRateRepository {

    //TODO REPLACE THIS METHOD WITH NEW realGetCurrencyRates()
    suspend fun getCurrencyRates(): Map<String, Double>

    suspend fun realGetCurrencyRates(
        mainCurrency: Currency,
        otherCurrencies: List<Currency>
    ): Map<Currency, BigDecimal>

}