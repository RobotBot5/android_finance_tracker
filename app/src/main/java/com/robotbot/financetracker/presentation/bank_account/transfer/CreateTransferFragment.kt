package com.robotbot.financetracker.presentation.bank_account.transfer

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.robotbot.financetracker.databinding.FragmentCreateTransferBinding
import com.robotbot.financetracker.domain.DomainConstants
import com.robotbot.financetracker.domain.entities.BankAccountEntity
import com.robotbot.financetracker.presentation.FinanceTrackerApp
import com.robotbot.financetracker.presentation.ViewModelFactory
import com.robotbot.financetracker.presentation.bank_account.transfer.choose_account.ChooseAccountDialog
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.Calendar
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
        setListenersOnViews()
        observeViewModel()
    }

    private fun setListenersOnViews() {
        with(binding) {
            llFromAccountContainer.setOnClickListener {
                showDialogToChooseAccount(viewModel.state.value.accountFrom) { chosenBankAccountId ->
                    viewModel.setFromAccount(chosenBankAccountId)
                }
            }
            llToAccountContainer.setOnClickListener {
                showDialogToChooseAccount(viewModel.state.value.accountTo) { chosenBankAccountId ->
                    viewModel.setToAccount(chosenBankAccountId)
                }
            }
            etTransferAmountFrom.doOnTextChanged { input, _, _, _ ->
                viewModel.setAmountFrom(input.toString())
            }
            etTransferAmountTo.doOnTextChanged { input, _, _, _ ->
                viewModel.setAmountTo(input.toString())
            }
            btnSaveTransfer.setOnClickListener {
                viewModel.saveTransfer()
            }
            tvSelectedDate.setOnClickListener {
                findNavController().navigate(
                    CreateTransferFragmentDirections.actionCreateTransferFragmentToDatePickerFragment (
                        viewModel.state.value.selectedDate
                    )
                )
                setFragmentResultListener(DatePickerFragment.REQUEST_KEY) { _, bundle ->
                    bundle.serializable<Calendar>(DatePickerFragment.RESULT_KEY)?.let {
                        viewModel.setDate(it)
                    }
                }
            }
        }
    }

    private fun showDialogToChooseAccount(
        currentBankAccount: BankAccountEntity?,
        onResultListener: (chosenBankAccountId: Int) -> Unit
    ) {
        val currentBankAccountId = currentBankAccount?.id ?: DomainConstants.UNDEFINED_ID
        findNavController().navigate(
            CreateTransferFragmentDirections.actionCreateTransferFragmentToChooseAccountDialog(
                currentBankAccountId
            )
        )
        setFragmentResultListener(ChooseAccountDialog.REQUEST_KEY) { _, bundle ->
            val bankAccountId = bundle.getInt(ChooseAccountDialog.RESULT_KEY)
            onResultListener(bankAccountId)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect {
                    with(binding) {
                        btnSaveTransfer.isEnabled = it.saveButtonEnabled
                        tvSelectedDate.text = viewModel.formatDateToString(it.selectedDate)
                        if (it.accountFrom != null) {
                            tvFromAccount.text = it.accountFrom.name
                        } else {
                            tvFromAccount.text = "Not specified"
                        }
                        if (it.accountTo != null) {
                            tvToAccount.text = it.accountTo.name
                        } else {
                            tvToAccount.text = "Not specified"
                        }
                        when (it.displayState) {
                            CreateTransferDisplayState.SingleCurrency -> {
                                tilTransferAmountTo.visibility = GONE
                            }
                            is CreateTransferDisplayState.DifferentCurrencies -> {
                                tilTransferAmountTo.visibility = VISIBLE
                                etTransferAmountTo.setHint(it.displayState.amountToPlaceholder.toPlainString())
                            }
                            CreateTransferDisplayState.WorkEnded -> {
                                findNavController().popBackStack()
                            }

                            is CreateTransferDisplayState.Error -> {
                                val errorText = when (it.displayState.errorState) {
                                    ErrorState.InsufficientFunds -> {
                                        "Insufficient funds, check your balance"
                                    }
                                    ErrorState.InvalidTransfer -> {
                                        "Invalid transfer settings, contact us"
                                    }
                                    ErrorState.UnknownError -> {
                                        "Unknown error, contact us"
                                    }
                                }
                                Toast.makeText(requireActivity(), errorText, Toast.LENGTH_SHORT)
                                    .show()
                                findNavController().popBackStack()
                            }
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

    private inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializable(key) as? T
    }
}