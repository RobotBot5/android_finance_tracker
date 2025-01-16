package com.robotbot.financetracker.presentation.bank_account.transfer.choose_account

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.robotbot.financetracker.databinding.ItemChooseBankAccountBinding
import com.robotbot.financetracker.domain.entities.BankAccountEntity
import com.robotbot.financetracker.presentation.bank_account.adapter.BankAccountDiffCallback
import java.text.DecimalFormat
import java.util.Locale

class ChooseAccountAdapter(private val currentAccountId: Int) :
    ListAdapter<BankAccountEntity, ChooseAccountAdapter.ChooseBankAccountViewHolder>(BankAccountDiffCallback) {

    var onAccountClickListener: ((BankAccountEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseBankAccountViewHolder {
        val binding = ItemChooseBankAccountBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChooseBankAccountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChooseBankAccountViewHolder, position: Int) {
        val bankAccount = getItem(position)
        with(holder.binding) {
            with(bankAccount) {
                if (bankAccount.id == currentAccountId) {
                    tvAccountName.setTextColor(Color.RED)
                }
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

    class ChooseBankAccountViewHolder(val binding: ItemChooseBankAccountBinding) : ViewHolder(binding.root)
}