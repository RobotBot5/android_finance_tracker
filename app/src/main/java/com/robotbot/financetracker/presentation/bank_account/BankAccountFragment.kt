package com.robotbot.financetracker.presentation.bank_account

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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

    private lateinit var viewModel: BankAccountViewModel

    @Inject
    lateinit var bankAccountsAdapter: BankAccountsAdapter

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
            viewModelFactory
        )[BankAccountViewModel::class.java]
        setupRecyclerView()
        setupListenersOnViews()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        bankAccountsAdapter.onAccountClickListener = {
            val intent = ManageBankAccountActivity.newIntentEditMode(requireContext(), it.id)
            startActivity(intent)
        }
        binding.rvBankAccounts.adapter = bankAccountsAdapter

    }

    private fun setupListenersOnViews() {
        binding.fabAddAccount.setOnClickListener {
            val intent = ManageBankAccountActivity.newIntentAddMode(requireContext())
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect {
                    binding.pbBankAccounts.visibility = GONE
                    when (it.displayState) {
                        BankAccountDisplayState.Initial -> {  }
                        BankAccountDisplayState.Loading -> {
                            binding.pbBankAccounts.visibility = VISIBLE
                        }
                        is BankAccountDisplayState.Content -> {
                            bankAccountsAdapter.submitList(it.displayState.accounts)
                            binding.tvTotalBalance.text = "${it.displayState.totalBalance} руб."
                        }
                    }
                }
            }
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