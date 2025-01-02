package com.robotbot.financetracker.presentation.bank_account

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.robotbot.financetracker.presentation.FinanceTrackerApp
import com.robotbot.financetracker.databinding.FragmentBankAccountsBinding
import com.robotbot.financetracker.presentation.ViewModelFactory
import com.robotbot.financetracker.presentation.bank_account.adapter.BankAccountsAdapter
import com.robotbot.financetracker.presentation.bank_account.manage.ManageBankAccountActivity
import kotlinx.coroutines.launch
import javax.inject.Inject

class BankAccountFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as FinanceTrackerApp).component
    }

    private var _binding: FragmentBankAccountsBinding? = null
    private val binding: FragmentBankAccountsBinding
        get() = _binding ?: throw RuntimeException("FragmentBankAccountsBinding == null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var bankAccountsAdapter: BankAccountsAdapter

    private lateinit var viewModel: BankAccountViewModel

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

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
        viewModel = ViewModelProvider(
            this,
            viewModelFactory)[BankAccountViewModel::class.java]
        lifecycleScope.launch {
            viewModel.bankAccounts.collect {
                bankAccountsAdapter.submitList(it)
            }
        }
        bankAccountsAdapter.onAccountClickListener = {
            val intent = ManageBankAccountActivity.newIntentEditMode(requireContext(), it.id)
            startActivity(intent)
        }
        binding.rvBankAccounts.adapter = bankAccountsAdapter
        binding.fabAddAccount.setOnClickListener {
            val intent = ManageBankAccountActivity.newIntentAddMode(requireContext())
            startActivity(intent)
        }
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