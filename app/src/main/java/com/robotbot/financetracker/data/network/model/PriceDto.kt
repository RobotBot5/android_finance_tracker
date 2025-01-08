package com.robotbot.financetracker.data.network.model

import com.google.gson.annotations.SerializedName

data class PriceDto(
    @SerializedName("RUB") val rub: String
)
