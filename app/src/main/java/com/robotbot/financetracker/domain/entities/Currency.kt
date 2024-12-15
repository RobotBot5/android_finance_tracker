package com.robotbot.financetracker.domain.entities

enum class Currency(val symbol: String, val code: String) {

    USD("$", "USD"),
    RUB("₽", "RUB"),
    EUR("€", "EUR")

}