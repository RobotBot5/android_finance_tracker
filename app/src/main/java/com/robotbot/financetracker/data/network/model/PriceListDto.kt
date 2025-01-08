package com.robotbot.financetracker.data.network.model

import com.google.gson.annotations.SerializedName

data class PriceListDto(
    @SerializedName("BTC") val btc: PriceDto,
    @SerializedName("USD") val usd: PriceDto,
    @SerializedName("EUR") val eur: PriceDto
)
