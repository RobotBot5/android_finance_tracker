package com.robotbot.financetracker.presentation.bank_account.manage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.robotbot.financetracker.databinding.FragmentManageBankAccountBinding
import com.robotbot.financetracker.domain.DomainConstants
import com.robotbot.financetracker.domain.entities.Currency
import com.robotbot.financetracker.presentation.FinanceTrackerApp
import com.robotbot.financetracker.presentation.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class ManageBankAccountFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as FinanceTrackerApp).component
    }

    private var _binding: FragmentManageBankAccountBinding? = null
    private val binding: FragmentManageBankAccountBinding
        get() = _binding ?: throw RuntimeException("FragmentManageBankAccountBinding == null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: ManageBankAccountViewModel

    private lateinit var onWorkEndedListener: OnWorkEndedListener

    interface OnWorkEndedListener {
        fun onWorkEndedListener()
    }

    private var screenMode: String = MODE_UNDEFINED

    private var accountId: Int = DomainConstants.UNDEFINED_ID

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
        if (context is OnWorkEndedListener) {
            onWorkEndedListener = context
        } else {
            throw RuntimeException("Activity must implement OnWorkEndedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Screen mode param is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(ACCOUNT_ID)) {
                throw RuntimeException("Account id param is absent")
            }
            accountId = args.getInt(ACCOUNT_ID, DomainConstants.UNDEFINED_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageBankAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[ManageBankAccountViewModel::class.java]
        // TODO: Replace temporary spnCurrency logic with a recycler view (mb on another activity/fragment)
        binding.spnCurrency.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            Currency.entries
        )
        binding.spnCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setCurrencyToState(parent?.getItemAtPosition(position) as Currency)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        launchRightMode()
        observeViewModel()
        setListenersOnViews()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect {
                    with(binding) {
                        tilAccountBalance.suffixText = it.selectedCurrency.toString()
                        when (it.displayState) {
                            ManageBankAccountDisplayState.Initial -> {
                            }
                            is ManageBankAccountDisplayState.Content -> {
                                tilAccountName.error = it.displayState.nameError
                                tilAccountBalance.error = it.displayState.balanceError
                            }

                            is ManageBankAccountDisplayState.InitialEditMode -> {
                                etAccountName.setText(it.displayState.accountEntity.name)
                                etAccountBalance.setText(it.displayState.accountEntity.balance.toPlainString())
                            }

                            is ManageBankAccountDisplayState.Loading -> {

                            }

                            is ManageBankAccountDisplayState.WorkEnded -> {
                                onWorkEndedListener.onWorkEndedListener()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setListenersOnViews() {
        with(binding) {
            etAccountName.doOnTextChanged { _, _, _, _ ->
                viewModel.resetErrorInputName()
            }
            etAccountBalance.doOnTextChanged { _, _, _, _ ->
                viewModel.resetErrorInputBalance()
            }
        }
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun launchEditMode() {
        binding.spnCurrency.visibility = GONE
        binding.btnDeleteAccount.visibility = VISIBLE
        viewModel.setupAccountToEditById(accountId)
        with(binding) {
            btnSaveAccount.setOnClickListener {
                viewModel.editAccount(
                    etAccountName.text.toString(),
                    etAccountBalance.text.toString()
                )
            }
            btnDeleteAccount.setOnClickListener {
                lifecycleScope.launch {
                    val accountName = viewModel.state.value.accountToDeleteName ?: return@launch
                    parentFragmentManager.setFragmentResultListener(
                        DeleteBankAccountDialogFragment.REQUEST_KEY,
                        viewLifecycleOwner
                    ) { _, bundle ->
                        val confirmed = bundle.getBoolean(DeleteBankAccountDialogFragment.RESULT_KEY)
                        if (confirmed) {
                            viewModel.deleteAccount()
                        }
                    }
                    DeleteBankAccountDialogFragment.newInstance(
                        accountName = accountName
                    ).show(parentFragmentManager, "DELETE_ACCOUNT_DIALOG")
                }
            }
        }
    }

    private fun launchAddMode() {
        with(binding) {
            btnSaveAccount.setOnClickListener {
                viewModel.addAccount(
                    etAccountName.text.toString(),
                    etAccountBalance.text.toString()
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val SCREEN_MODE = "screen_mode"
        private const val MODE_ADD = "add_mode"
        private const val MODE_EDIT = "edit_mode"
        private const val MODE_UNDEFINED = ""
        private const val ACCOUNT_ID = "category_id"

        fun newInstanceAddAccount(): ManageBankAccountFragment {
            return ManageBankAccountFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditAccount(accountId: Int): ManageBankAccountFragment {
            return ManageBankAccountFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(ACCOUNT_ID, accountId)
                }
            }
        }
    }

}