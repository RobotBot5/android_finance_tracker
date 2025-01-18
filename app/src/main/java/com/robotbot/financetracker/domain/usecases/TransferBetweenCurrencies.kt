package com.robotbot.financetracker.domain.usecases

import com.robotbot.financetracker.domain.entities.Currency
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class TransferBetweenCurrencies @Inject constructor() {

    operator fun invoke(
        amount: BigDecimal,
        currencyPair: Pair<Currency, Currency>,
        currencyRates: Map<Currency, BigDecimal>
    ): BigDecimal {

        val currencyRateFrom = currencyRates[currencyPair.first] ?: return BigDecimal.ZERO
        val currencyRateTo = currencyRates[currencyPair.second] ?: return BigDecimal.ZERO

        val inUSD = amount.divide(
            currencyRateFrom,
            10,
            RoundingMode.HALF_UP
        )
        return inUSD * currencyRateTo
    }
}