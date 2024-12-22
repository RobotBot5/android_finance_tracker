package com.robotbot.financetracker.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.robotbot.financetracker.FinanceTrackerApp
import com.robotbot.financetracker.R
import com.robotbot.financetracker.databinding.ActivityCreateBankAccountBinding
import com.robotbot.financetracker.domain.DomainConstants
import com.robotbot.financetracker.domain.entities.Currency
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateBankAccountActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCreateBankAccountBinding.inflate(layoutInflater)
    }

    private val component by lazy {
        (application as FinanceTrackerApp).component
    }

    private val viewModel: CreateBankAccountViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var screenMode: String = MODE_UNDEFINED

    private var accountId: Int = DomainConstants.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        parseParams()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // TODO: Replace temporary spnCurrency logic with a recycler view (mb on another activity/fragment)
        binding.spnCurrency.adapter = ArrayAdapter(
            this,
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
                viewModel.setCurrency(parent?.getItemAtPosition(position) as Currency)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        observeViewModel()
        setListenersOnViews()
    }

    private fun parseParams() {
        screenMode = intent.getStringExtra(EXTRA_SCREEN_MODE)
            ?: throw RuntimeException("Extra screen mode is absent")
        if (screenMode != MODE_EDIT && screenMode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $screenMode")
        }
        if (screenMode == MODE_EDIT) {
            accountId = intent.getIntExtra(EXTRA_ACCOUNT_ID, 0)
            if (accountId == 0) {
                throw RuntimeException("Param account id is absent")
            }
        }
    }

    private fun setListenersOnViews() {
        with(binding) {
            btnSaveAccount.setOnClickListener {
                viewModel.addAccount(
                    etAccountName.text.toString(),
                    etAccountBalance.text.toString()
                )
            }
            etAccountName.doOnTextChanged { _, _, _, _ ->
                viewModel.resetErrorInputName()
            }
            etAccountBalance.doOnTextChanged { _, _, _, _ ->
                viewModel.resetErrorInputBalance()
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect {
                    with(binding) {
                        tilAccountName.error = it.nameError
                        tilAccountBalance.error = it.balanceError
                        tilAccountBalance.suffixText = it.selectedCurrency.toString()
                        if (it.isAccountCreated) {
                            finish()
                        }
                    }
                }
            }
        }
    }

    companion object {

        private const val EXTRA_SCREEN_MODE = "screen_mode"
        private const val MODE_ADD = "add_mode"
        private const val MODE_EDIT = "edit_mode"
        private const val MODE_UNDEFINED = ""
        private const val EXTRA_ACCOUNT_ID = "account_id"

        fun newIntentAddMode(context: Context): Intent {
            return Intent(context, CreateBankAccountActivity::class.java).apply {
                putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            }
        }

        fun newIntentEditMode(context: Context, accountId: Int): Intent {
            return Intent(context, CreateBankAccountActivity::class.java).apply {
                putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
                putExtra(EXTRA_ACCOUNT_ID, accountId)
            }
        }

    }
}