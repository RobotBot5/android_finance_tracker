package com.robotbot.financetracker.presentation.category_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.robotbot.financetracker.databinding.ItemCategoryBinding
import com.robotbot.financetracker.domain.entities.TransactionCategoryEntity
import javax.inject.Inject

class CategoryAdapter @Inject constructor() :
    ListAdapter<TransactionCategoryEntity, CategoryViewHolder>(CategoryDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

    }
}