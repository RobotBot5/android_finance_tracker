package com.robotbot.financetracker.data.repository

import com.robotbot.financetracker.data.network.ApiService
import com.robotbot.financetracker.di.ApplicationScope
import com.robotbot.financetracker.domain.entities.Currency
import com.robotbot.financetracker.domain.repotisories.CurrencyRateRepository
import javax.inject.Inject

@ApplicationScope
class CurrencyRateRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : CurrencyRateRepository {

    private var currencyRates: Map<String, Double>? = null

    override suspend fun getCurrencyRates(): Map<String, Double> {
        val currentList = currencyRates
        if (currentList != null) return currentList
        val response = apiService.getCoinPrices()
        return mapOf(
            Currency.USD.name to response.usd.rub,
            Currency.EUR.name to response.eur.rub,
            Currency.BTC.name to response.btc.rub
        ).also {
            currencyRates = it
        }
    }
}