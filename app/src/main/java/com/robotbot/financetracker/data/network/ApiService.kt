package com.robotbot.financetracker.data.network

import com.robotbot.financetracker.data.network.model.PriceListDto
import retrofit2.http.GET

interface ApiService {

    @GET("pricemulti?fsyms=BTC,USD,EUR&tsyms=RUB")
    suspend fun getCoinPrices(): PriceListDto

}