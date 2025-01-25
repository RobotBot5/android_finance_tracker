package com.robotbot.financetracker.presentation.bank_account

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navGraphViewModels
import com.robotbot.financetracker.R
import com.robotbot.financetracker.databinding.FragmentBankAccountListBinding
import com.robotbot.financetracker.presentation.FinanceTrackerApp
import com.robotbot.financetracker.presentation.ViewModelFactory
import com.robotbot.financetracker.presentation.bank_account.adapter.BankAccountsAdapter
import com.robotbot.financetracker.presentation.navigation.ManageMode
import kotlinx.coroutines.launch
import javax.inject.Inject

class BankAccountListFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as FinanceTrackerApp).component
    }

    private var _binding: FragmentBankAccountListBinding? = null
    private val binding: FragmentBankAccountListBinding
        get() = _binding ?: throw RuntimeException("FragmentBankAccountListBinding == null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: BankAccountViewModel by viewModels({requireParentFragment().requireParentFragment()}) { viewModelFactory }

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
        _binding = FragmentBankAccountListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("viewModelTest", "List: ${viewModel.test}")
        Log.d("viewModelTest", "Parent fragment: ${requireParentFragment().requireParentFragment()::class.java}")
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
//        bankAccountsAdapter.onAccountClickListener = {
//            navController.navigate(
//                BankAccountFragmentDirections.actionBankAccountsFragmentToManageBankAccountFragment(
//                    ManageMode.EDIT
//                ).apply {
//                    accountId = it.id
//                }
//            )
//        }
        binding.rvBankAccounts.adapter = bankAccountsAdapter

    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.state.collect {
                    when (it.bankAccountListState) {
                        BankAccountListState.Initial -> {}
                        BankAccountListState.Loading -> {
                            binding.pbBankAccounts.visibility = VISIBLE
                            binding.rvBankAccounts.visibility = GONE
                        }

                        is BankAccountListState.Content -> {
                            binding.pbBankAccounts.visibility = GONE
                            binding.rvBankAccounts.visibility = VISIBLE
                            bankAccountsAdapter.submitList(it.bankAccountListState.accounts)
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

}