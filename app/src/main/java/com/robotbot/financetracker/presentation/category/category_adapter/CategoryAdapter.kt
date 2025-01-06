package com.robotbot.financetracker.presentation.category.category_adapter

import android.app.Application
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.robotbot.financetracker.R
import com.robotbot.financetracker.databinding.ItemCategoryBinding
import com.robotbot.financetracker.domain.entities.CategoryEntity
import javax.inject.Inject

class CategoryAdapter @Inject constructor(private val application: Application) :
    ListAdapter<CategoryEntity, CategoryViewHolder>(CategoryDiffCallback) {

    var onAddButtonClickListener: (() -> Unit)? = null
    var onCategoryClickListener: ((CategoryEntity) -> Unit)? = null

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
                tvCategoryTitle.text = category.name
                val uri = Uri.parse("android.resource://${application.packageName}/drawable/${category.iconResName}")
                ivCategoryIcon.setImageURI(uri)

                root.setOnClickListener {
                    onCategoryClickListener?.invoke(category)
                }
            }
        } else if (getItemViewType(position) == VIEW_ADD_BUTTON_TYPE) {
            with(holder.binding) {
                tvCategoryTitle.text =
                    holder.binding.root.context.getString(R.string.btn_category_add)
                root.setOnClickListener {
                    onAddButtonClickListener?.invoke()
                }
                ivCategoryIcon.setImageResource(android.R.drawable.ic_menu_add)
            }
        }
    }
}