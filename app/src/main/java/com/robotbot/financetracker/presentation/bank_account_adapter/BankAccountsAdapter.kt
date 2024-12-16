package com.robotbot.financetracker.presentation.bank_account_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.robotbot.financetracker.databinding.ItemBankAccountBinding
import com.robotbot.financetracker.domain.entities.BankAccountEntity
import javax.inject.Inject

class BankAccountsAdapter @Inject constructor() :
    ListAdapter<BankAccountEntity, BankAccountViewHolder>(BankAccountDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankAccountViewHolder {
        val binding = ItemBankAccountBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BankAccountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BankAccountViewHolder, position: Int) {
        val bankAccount = getItem(position)
        holder.binding.tvAccountName.text = bankAccount.name
        holder.binding.tvAccountBalance.text = bankAccount.balance.toString()
    }
}