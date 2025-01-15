package com.robotbot.financetracker.presentation.bank_account.transfer

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
import com.robotbot.financetracker.databinding.FragmentCreateTransferBinding
import com.robotbot.financetracker.presentation.FinanceTrackerApp
import com.robotbot.financetracker.presentation.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateTransferFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as FinanceTrackerApp).component
    }

    private var _binding: FragmentCreateTransferBinding? = null
    private val binding: FragmentCreateTransferBinding
        get() = _binding ?: throw RuntimeException("FragmentCreateTransferBinding == null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: CreateTransferViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateTransferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect {
                    when (it.displayState) {
                        CreateTransferDisplayState.Initial -> {}
                        CreateTransferDisplayState.Loading -> {
                            binding.pbLoadingScreen.visibility = VISIBLE
                            binding.clMainContent.visibility = GONE
                        }
                        CreateTransferDisplayState.Loaded -> {
                            binding.pbLoadingScreen.visibility = GONE
                            binding.clMainContent.visibility = VISIBLE
                            Log.d("CreateTransferFragment", viewModel.bankAccounts.toString())
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