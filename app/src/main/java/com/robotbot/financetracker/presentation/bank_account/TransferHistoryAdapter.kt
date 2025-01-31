package com.robotbot.financetracker.presentation.bank_account

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.robotbot.financetracker.databinding.ItemTransferDifferentCurrenciesBinding
import com.robotbot.financetracker.databinding.ItemTransferSingleCurrencyBinding
import com.robotbot.financetracker.domain.entities.TransferEntity
import javax.inject.Inject

class TransferHistoryAdapter @Inject constructor() :
    ListAdapter<TransferEntity, TransferHistoryAdapter.TransferViewHolder>(TransferDiffCallback) {

    var onTransferClickListener: ((TransferEntity) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        val transfer = getItem(position)
        return if (transfer.accountFrom.currency == transfer.accountTo.currency)
            VIEW_SINGLE_CURRENCY_TYPE
        else
            VIEW_DIFFERENT_CURRENCIES_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransferViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = when (viewType) {
            VIEW_SINGLE_CURRENCY_TYPE -> ItemTransferSingleCurrencyBinding.inflate(
                layoutInflater, parent, false
            )

            VIEW_DIFFERENT_CURRENCIES_TYPE -> ItemTransferDifferentCurrenciesBinding.inflate(
                layoutInflater,
                parent,
                false
            )

            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        return TransferViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransferViewHolder, position: Int) {
        val binding = holder.binding
        val transfer = getItem(position)
        binding.root.setOnClickListener {
            onTransferClickListener?.invoke(transfer)
        }
        when (binding) {
            is ItemTransferSingleCurrencyBinding -> {
                binding.tvAccountFrom.text = transfer.accountFrom.name
                binding.tvAccountTo.text = transfer.accountTo.name
                binding.tvAmount.text = transfer.amountFrom.toPlainString()
            }

            is ItemTransferDifferentCurrenciesBinding -> {
                binding.tvAccountFrom.text = transfer.accountFrom.name
                binding.tvAccountTo.text = transfer.accountTo.name
                binding.tvAmountFrom.text = transfer.amountFrom.toPlainString()
                binding.tvAmountTo.text = transfer.amountTo.toPlainString()
            }
        }
    }

    companion object {
        private const val VIEW_SINGLE_CURRENCY_TYPE = 0
        private const val VIEW_DIFFERENT_CURRENCIES_TYPE = 1
    }

    class TransferViewHolder(val binding: ViewBinding) : ViewHolder(binding.root)
}