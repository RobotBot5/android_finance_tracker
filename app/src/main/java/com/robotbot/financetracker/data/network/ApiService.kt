package com.robotbot.financetracker.data.network

import com.google.gson.JsonObject
import com.robotbot.financetracker.data.network.model.PriceListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("pricemulti?fsyms=BTC,USD,EUR&tsyms=RUB")
    suspend fun getCoinPrices(): PriceListDto

    @GET("price")
    suspend fun getCurrencyRates(
        @Query(QUERY_PARAM_FROM_SYMBOL) mainCurrencyCode: String = "RUB",
        @Query(QUERY_PARAM_TO_SYMBOLS) otherCurrencyList: String
    ): JsonObject

    companion object {

        private const val QUERY_PARAM_FROM_SYMBOL = "fsym"
        private const val QUERY_PARAM_TO_SYMBOLS = "tsyms"
    }

}