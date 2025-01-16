package com.robotbot.financetracker.presentation.bank_account.transfer.choose_account

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.robotbot.financetracker.databinding.ChooseAccountDialogBinding
import com.robotbot.financetracker.presentation.FinanceTrackerApp
import com.robotbot.financetracker.presentation.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChooseAccountDialog : DialogFragment() {

    private val component by lazy {
        (requireActivity().application as FinanceTrackerApp).component
    }

    private val args by navArgs<ChooseAccountDialogArgs>()

    private var _binding: ChooseAccountDialogBinding? = null
    private val binding: ChooseAccountDialogBinding
        get() = _binding ?: throw RuntimeException("ChooseAccountDialogBinding == null")

    private val chooseAccountAdapter by lazy {
        ChooseAccountAdapter(args.currentAccountId)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: ChooseAccountViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        component.inject(this)
        Log.d("ChooseAccountDialog", args.currentAccountId.toString())
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            _binding = ChooseAccountDialogBinding.inflate(layoutInflater)
            setupRecyclerView()
            observeViewModel()

            return AlertDialog.Builder(it).setView(binding.root)
                .setNegativeButton("Cancel") { _, _ ->
                }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setupRecyclerView() {
        binding.rvAccounts.adapter = chooseAccountAdapter
        chooseAccountAdapter.onAccountClickListener = {
            setFragmentResult(
                REQUEST_KEY,
                Bundle().apply { putInt(RESULT_KEY, it.id) }
            )
            findNavController().popBackStack()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    ChooseAccountState.Initial -> {}
                    ChooseAccountState.Loading -> {
                        binding.pbLoadingScreen.visibility = VISIBLE
                        binding.rvAccounts.visibility = GONE
                    }

                    is ChooseAccountState.Loaded -> {
                        binding.pbLoadingScreen.visibility = GONE
                        binding.rvAccounts.visibility = VISIBLE
                        chooseAccountAdapter.submitList(it.bankAccounts)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("ChooseAccountDialog", "onDestroyView")
    }

    companion object {

        const val REQUEST_KEY = "choose_account_request"
        const val RESULT_KEY = "choose_account_result"

    }
}