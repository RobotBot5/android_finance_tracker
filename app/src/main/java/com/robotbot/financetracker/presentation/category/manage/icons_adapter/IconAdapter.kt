package com.robotbot.financetracker.presentation.category.manage.icons_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import com.robotbot.financetracker.databinding.ItemCategoryIconBinding
import com.robotbot.financetracker.domain.entities.CategoryIconEntity
import javax.inject.Inject

class IconAdapter @Inject constructor() :
    ListAdapter<CategoryIconEntity, IconViewHolder>(IconDiffCallback) {

    var onCategoryIconClickListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {

        val binding = ItemCategoryIconBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IconViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        val iconEntity = getItem(position)
        holder.binding.ivIcon.setImageResource(iconEntity.iconResId)
        val colorResId = if (iconEntity.isSelected) {
            android.R.color.holo_blue_dark
        } else {
            android.R.color.holo_blue_light
        }
        val color = ContextCompat.getColor(holder.binding.root.context, colorResId)
        holder.binding.ivIcon.setBackgroundColor(color)
        holder.binding.root.setOnClickListener { onCategoryIconClickListener?.invoke(iconEntity.iconResId) }
    }
}
