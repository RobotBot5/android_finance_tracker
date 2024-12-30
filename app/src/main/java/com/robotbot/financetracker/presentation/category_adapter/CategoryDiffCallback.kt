package com.robotbot.financetracker.presentation.category_adapter

import androidx.recyclerview.widget.DiffUtil
import com.robotbot.financetracker.domain.entities.TransactionCategoryEntity

object CategoryDiffCallback : DiffUtil.ItemCallback<TransactionCategoryEntity>() {

    override fun areItemsTheSame(
        oldItem: TransactionCategoryEntity,
        newItem: TransactionCategoryEntity
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: TransactionCategoryEntity,
        newItem: TransactionCategoryEntity
    ): Boolean {
        return oldItem == newItem
    }
}