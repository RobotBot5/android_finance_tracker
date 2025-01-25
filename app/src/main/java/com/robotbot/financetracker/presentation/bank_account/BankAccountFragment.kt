package com.robotbot.financetracker.presentation.bank_account

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.snackbar.Snackbar
import com.robotbot.financetracker.R
import com.robotbot.financetracker.databinding.FragmentBankAccountsBinding
import com.robotbot.financetracker.presentation.FinanceTrackerApp
import com.robotbot.financetracker.presentation.ViewModelFactory
import com.robotbot.financetracker.presentation.bank_account.adapter.BankAccountsAdapter
import com.robotbot.financetracker.presentation.navigation.ManageMode
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

    private val viewModel: BankAccountViewModel by viewModels { viewModelFactory }

    private val navController by lazy {
        findNavController()
    }

    private val nestedNavController by lazy {
        val nestedNavHostFragment =
            childFragmentManager.findFragmentById(R.id.frag_container) as NavHostFragment
        nestedNavHostFragment.navController
    }

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
        Log.d("viewModelTest", "List: ${viewModel.test}")
        setupListenersOnViews()
        observeViewModel()
    }

    private fun setupListenersOnViews() {
        binding.fabAddAccount.setOnClickListener {
            navController.navigate(
                BankAccountFragmentDirections.actionBankAccountsFragmentToManageBankAccountFragment(
                    ManageMode.ADD
                )
            )
        }

        binding.btnTransfer.setOnClickListener {
            nestedNavController.popBackStack(
                destinationId = nestedNavController.graph.startDestinationId,
                inclusive = false,
                saveState = true
            )
        }
        binding.btnTransferHistory.setOnClickListener {
            nestedNavController.navigate(R.id.transferListFragment)
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (!nestedNavController.popBackStack(
                    destinationId = nestedNavController.graph.findStartDestination().id,
                    inclusive = false
                )
            ) {
                navController.popBackStack(
                    navController.graph.findStartDestination().id,
                    inclusive = false,
                    saveState = true
                )
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.state.collect {
                    Log.d("BankAccountsState", "$it")
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
                            binding.tvTotalBalance.text =
                                "${it.totalBalanceState.totalBalance} руб."
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
        viewLifecycleOwner.lifecycleScope.launch {
            nestedNavController.currentBackStack.collect { list ->
                Log.d("BankAccountFragment", list.map { it.destination.label }.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}