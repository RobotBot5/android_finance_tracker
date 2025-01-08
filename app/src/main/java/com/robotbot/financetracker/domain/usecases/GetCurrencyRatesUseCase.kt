package com.robotbot.financetracker.domain.usecases

import com.robotbot.financetracker.domain.entities.TransferEntity
import com.robotbot.financetracker.domain.repotisories.CurrencyRateRepository
import com.robotbot.financetracker.domain.repotisories.TransferRepository
import javax.inject.Inject

class GetCurrencyRatesUseCase @Inject constructor(
    private val repository: CurrencyRateRepository
) {

    suspend operator fun invoke() = repository.getCurrencyRates()

}