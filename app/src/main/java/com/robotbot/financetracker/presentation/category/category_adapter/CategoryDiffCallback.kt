package com.robotbot.financetracker.presentation.category.category_adapter

import androidx.recyclerview.widget.DiffUtil
import com.robotbot.financetracker.domain.entities.CategoryEntity

object CategoryDiffCallback : DiffUtil.ItemCallback<CategoryEntity>() {

    override fun areItemsTheSame(
        oldItem: CategoryEntity,
        newItem: CategoryEntity
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: CategoryEntity,
        newItem: CategoryEntity
    ): Boolean {
        return oldItem == newItem
    }
}