package com.robotbot.financetracker.presentation.category.manage.icons_adapter

import android.app.Application
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.use
import androidx.recyclerview.widget.RecyclerView
import com.robotbot.financetracker.R
import com.robotbot.financetracker.databinding.ItemCategoryIconBinding
import javax.inject.Inject

class IconAdapter @Inject constructor(application: Application) :
    RecyclerView.Adapter<IconViewHolder>() {

    private val icons =
        application.resources.obtainTypedArray(R.array.category_icons).use { array ->
            List(array.length()) { array.getResourceId(it, 0) }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val binding = ItemCategoryIconBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        Log.d("IconAdapter", icons.toString())
        return IconViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        val iconResId = icons[position]
        holder.binding.ivIcon.setImageResource(iconResId)
    }

    override fun getItemCount(): Int = icons.size
}
