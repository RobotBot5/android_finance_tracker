package com.robotbot.financetracker.presentation.category_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.robotbot.financetracker.databinding.ItemCategoryBinding
import com.robotbot.financetracker.domain.entities.TransactionCategoryEntity
import javax.inject.Inject

class CategoryAdapter @Inject constructor() :
    ListAdapter<TransactionCategoryEntity, CategoryViewHolder>(CategoryDiffCallback) {

    private companion object {
        const val VIEW_CATEGORY_TYPE = 0
        const val VIEW_ADD_BUTTON_TYPE = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == currentList.size) VIEW_ADD_BUTTON_TYPE else VIEW_CATEGORY_TYPE
    }

    override fun getItemCount(): Int {
        return currentList.size + 1 // Add "Add category" button
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_CATEGORY_TYPE) {
            val category = getItem(position)
            with(holder.binding) {
                with(category) {
                    tvCategoryTitle.text = name
                }
            }
        } else if (getItemViewType(position) == VIEW_ADD_BUTTON_TYPE) {
            with(holder.binding) {
                tvCategoryTitle.text = "Add category"
            }
        }
    }
}