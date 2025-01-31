package com.robotbot.financetracker.presentation.bank_account

import android.content.Context
import android.os.Bundle
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
import androidx.navigation.findNavController
import com.robotbot.financetracker.R
import com.robotbot.financetracker.databinding.FragmentTransferHistoryBinding
import com.robotbot.financetracker.presentation.FinanceTrackerApp
import com.robotbot.financetracker.presentation.ViewModelFactory
import com.robotbot.financetracker.presentation.navigation.ManageMode
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class TransferListFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as FinanceTrackerApp).component
    }

    private var _binding: FragmentTransferHistoryBinding? = null
    private val binding: FragmentTransferHistoryBinding
        get() = _binding ?: throw RuntimeException("FragmentTransferHistoryBinding == null")

    @Inject
    lateinit var transferHistoryAdapter: TransferHistoryAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: TransferHistoryViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransferHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvTransfers.adapter = transferHistoryAdapter
        transferHistoryAdapter.onTransferClickListener = {
            requireActivity().findNavController(R.id.nav_host_fragment).navigate(
                BankAccountFragmentDirections.actionBankAccountsFragmentToCreateTransferFragment(
                    ManageMode.EDIT
                ).apply {
                    transferId = it.id
                }
            )
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.state.collect {
                    when (it) {
                        TransferHistoryState.Initial -> {}
                        TransferHistoryState.Loading -> {
                            binding.pbTransfers.visibility = VISIBLE
                            binding.rvTransfers.visibility = GONE
                        }
                        is TransferHistoryState.Content -> {
                            binding.pbTransfers.visibility = GONE
                            binding.rvTransfers.visibility = VISIBLE
                            transferHistoryAdapter.submitList(it.transfers)
                        }
                        TransferHistoryState.Error -> {

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