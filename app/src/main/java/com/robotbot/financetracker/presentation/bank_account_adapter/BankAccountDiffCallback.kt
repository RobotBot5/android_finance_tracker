package com.robotbot.financetracker.presentation.bank_account_adapter

import androidx.recyclerview.widget.DiffUtil
import com.robotbot.financetracker.domain.entities.BankAccountEntity

object BankAccountDiffCallback : DiffUtil.ItemCallback<BankAccountEntity>() {

    override fun areItemsTheSame(oldItem: BankAccountEntity, newItem: BankAccountEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: BankAccountEntity,
        newItem: BankAccountEntity
    ): Boolean {
        return oldItem == newItem
    }
}