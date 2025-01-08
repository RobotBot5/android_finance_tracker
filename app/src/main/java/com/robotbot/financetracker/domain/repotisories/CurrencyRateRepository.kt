package com.robotbot.financetracker.domain.repotisories

interface CurrencyRateRepository {

    suspend fun getCurrencyRates(): Map<String, Double>

}