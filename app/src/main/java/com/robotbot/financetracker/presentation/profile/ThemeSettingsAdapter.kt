package com.robotbot.financetracker.presentation.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.robotbot.financetracker.databinding.ItemThemeSettingNotSelectedBinding
import com.robotbot.financetracker.databinding.ItemThemeSettingSelectedBinding
import com.robotbot.financetracker.domain.entities.SettingToDisplayEntity
import javax.inject.Inject

class ThemeSettingsAdapter @Inject constructor() :
    ListAdapter<SettingToDisplayEntity, ThemeSettingsAdapter.ThemeSettingViewHolder>(
        ThemeSettingDiffCallback
    ) {

    var onSettingClickListener: ((SettingToDisplayEntity) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        val setting = getItem(position)
        return if (!setting.isSelected) NOT_SELECTED_TYPE else SELECTED_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeSettingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = when (viewType) {
            NOT_SELECTED_TYPE -> {
                ItemThemeSettingNotSelectedBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            }

            SELECTED_TYPE -> {
                ItemThemeSettingSelectedBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            }

            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        return ThemeSettingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ThemeSettingViewHolder, position: Int) {
        val binding = holder.binding
        val setting = getItem(position)
        binding.root.setOnClickListener {
            onSettingClickListener?.invoke(setting)
        }
        when (binding) {
            is ItemThemeSettingSelectedBinding -> {
                binding.tvName.text = setting.name
            }

            is ItemThemeSettingNotSelectedBinding -> {
                binding.tvName.text = setting.name
            }
        }
    }

    companion object {
        const val NOT_SELECTED_TYPE = 0
        const val SELECTED_TYPE = 1
    }

    class ThemeSettingViewHolder(val binding: ViewBinding) : ViewHolder(binding.root)
}