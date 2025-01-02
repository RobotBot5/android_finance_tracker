package com.robotbot.financetracker.presentation.bank_account.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.robotbot.financetracker.databinding.ItemBankAccountBinding
import com.robotbot.financetracker.domain.entities.BankAccountEntity
import java.text.DecimalFormat
import java.util.Locale
import javax.inject.Inject

class BankAccountsAdapter @Inject constructor() :
    ListAdapter<BankAccountEntity, BankAccountViewHolder>(BankAccountDiffCallback) {

    var onAccountClickListener: ((BankAccountEntity) -> Unit)? = null

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
        with(holder.binding) {
            with(bankAccount) {
                tvAccountName.text = name
                val decimalFormat = DecimalFormat("#,##0.##")
                tvAccountBalance.text = String.format(
                    Locale.getDefault(),
                    "%s%s",
                    currency.symbol,
                    decimalFormat.format(balance)
                )
                root.setOnClickListener {
                    onAccountClickListener?.invoke(this)
                }
            }
        }
    }
}