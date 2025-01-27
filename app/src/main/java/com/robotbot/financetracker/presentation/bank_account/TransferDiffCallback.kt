package com.robotbot.financetracker.presentation.bank_account

import androidx.recyclerview.widget.DiffUtil
import com.robotbot.financetracker.domain.entities.TransferEntity

object TransferDiffCallback : DiffUtil.ItemCallback<TransferEntity>() {

    override fun areItemsTheSame(oldItem: TransferEntity, newItem: TransferEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TransferEntity, newItem: TransferEntity): Boolean {
        return oldItem == newItem
    }
}