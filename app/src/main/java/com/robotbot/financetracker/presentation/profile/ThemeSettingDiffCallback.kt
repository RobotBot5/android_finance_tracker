package com.robotbot.financetracker.presentation.profile

import androidx.recyclerview.widget.DiffUtil
import com.robotbot.financetracker.domain.entities.SettingToDisplayEntity

object ThemeSettingDiffCallback : DiffUtil.ItemCallback<SettingToDisplayEntity>() {

    override fun areItemsTheSame(
        oldItem: SettingToDisplayEntity,
        newItem: SettingToDisplayEntity
    ): Boolean {
        return oldItem.value == newItem.value
    }

    override fun areContentsTheSame(
        oldItem: SettingToDisplayEntity,
        newItem: SettingToDisplayEntity
    ): Boolean {
        return oldItem == newItem
    }
}