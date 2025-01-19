package com.robotbot.financetracker.domain.usecases

import com.robotbot.financetracker.domain.entities.Currency
import com.robotbot.financetracker.domain.repotisories.CurrencyRateRepository
import javax.inject.Inject

class GetCurrencyRatesUseCase @Inject constructor(
    private val repository: CurrencyRateRepository
) {

    //TODO REPLACE INVOKE WITH realInvoke()
    suspend operator fun invoke() = repository.getCurrencyRates()

    fun realInvoke(otherCurrencies: List<Currency>) =
        repository.realGetCurrencyRates(TARGET_CURRENCY, otherCurrencies)


    companion object {

        private val TARGET_CURRENCY = Currency.USD
    }
}