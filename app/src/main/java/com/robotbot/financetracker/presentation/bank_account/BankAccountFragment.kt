package com.robotbot.financetracker.presentation.bank_account

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
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
                    when (it.bankAccountListState) {
                        BankAccountListState.Initial -> {  }
                        BankAccountListState.Loading -> {
                            binding.pbBankAccounts.visibility = VISIBLE
                        }
                        is BankAccountListState.Content -> {
                            bankAccountsAdapter.submitList(it.bankAccountListState.accounts)
                        }
                    }
                    when (it.totalBalanceState) {
                        TotalBalanceState.Initial -> {}
                        TotalBalanceState.Loading -> {
                            binding.sflBalance.visibility = VISIBLE
                            binding.sflBalance.startShimmer()
                            binding.tvTotalBalance.visibility = INVISIBLE
                        }
                        is TotalBalanceState.Content -> {
                            binding.sflBalance.stopShimmer()
                            binding.sflBalance.visibility = GONE
                            binding.tvTotalBalance.visibility = VISIBLE
                            binding.tvTotalBalance.text = "${it.totalBalanceState.totalBalance} руб."
                        }
                        TotalBalanceState.Error -> {
                            binding.sflBalance.stopShimmer()
                            binding.sflBalance.visibility = GONE
                            binding.tvTotalBalance.visibility = VISIBLE
                            binding.tvTotalBalance.text = "Error"
                            Snackbar.make(
                                binding.clMain,
                                "Failed to load exchange rates. Please check your internet connection.",
                                Snackbar.LENGTH_INDEFINITE
                            )
                                .setAction("Retry") {
                                    viewModel.retryLoadBalance()
                                }
                                .show()
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