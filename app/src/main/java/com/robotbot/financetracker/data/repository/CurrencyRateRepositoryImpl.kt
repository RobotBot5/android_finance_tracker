package com.robotbot.financetracker.data.repository

import com.robotbot.financetracker.data.mapper.CurrencyRateMapper
import com.robotbot.financetracker.data.network.ApiService
import com.robotbot.financetracker.di.ApplicationScope
import com.robotbot.financetracker.domain.entities.Currency
import com.robotbot.financetracker.domain.repotisories.CurrencyRateRepository
import javax.inject.Inject

@ApplicationScope
class CurrencyRateRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: CurrencyRateMapper
) : CurrencyRateRepository {

    override suspend fun getCurrencyRates(): Map<String, Double> {
        val response = apiService.getCoinPrices()
        return mapOf(
            Currency.USD.name to response.usd.rub,
            Currency.EUR.name to response.eur.rub,
            Currency.BTC.name to response.btc.rub
        )
    }

    override suspend fun realGetCurrencyRates(
        mainCurrency: Currency,
        otherCurrencies: List<Currency>
    ) = mapper.mapApiResponseToMap(
        apiService.getCurrencyRates(
            mainCurrencyCode = mapper.mapMainCurrencyForQuery(mainCurrency),
            otherCurrencyList = mapper.mapOtherCurrencyForQuery(otherCurrencies)
        )
    )
}