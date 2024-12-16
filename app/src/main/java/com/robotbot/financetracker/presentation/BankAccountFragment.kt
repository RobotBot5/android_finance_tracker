package com.robotbot.financetracker.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.robotbot.financetracker.databinding.FragmentBankAccountsBinding

class BankAccountFragment : Fragment() {

    private var _binding: FragmentBankAccountsBinding? = null
    private val binding: FragmentBankAccountsBinding
        get() = _binding ?: throw RuntimeException("FragmentBankAccountsBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBankAccountsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): BankAccountFragment {
            return BankAccountFragment()
        }
    }

}