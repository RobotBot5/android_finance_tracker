package com.robotbot.financetracker.presentation.category.manage.icons_adapter

import androidx.recyclerview.widget.DiffUtil
import com.robotbot.financetracker.domain.entities.CategoryIconEntity

object IconDiffCallback : DiffUtil.ItemCallback<CategoryIconEntity>() {

    override fun areItemsTheSame(
        oldItem: CategoryIconEntity,
        newItem: CategoryIconEntity
    ): Boolean {
        return oldItem.iconResId == newItem.iconResId
    }

    override fun areContentsTheSame(
        oldItem: CategoryIconEntity,
        newItem: CategoryIconEntity
    ): Boolean {
        return oldItem == newItem
    }
}