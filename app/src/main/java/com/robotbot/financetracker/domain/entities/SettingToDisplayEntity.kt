package com.robotbot.financetracker.domain.entities

data class SettingToDisplayEntity(
    val name: String,
    val value: String,
    val isSelected: Boolean
)