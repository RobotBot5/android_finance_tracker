package com.robotbot.financetracker.presentation.bank_account.manage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
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
import com.robotbot.financetracker.presentation.FinanceTrackerApp
import com.robotbot.financetracker.R
import com.robotbot.financetracker.databinding.ActivityManageBankAccountBinding
import com.robotbot.financetracker.domain.DomainConstants
import com.robotbot.financetracker.domain.entities.Currency
import com.robotbot.financetracker.presentation.ViewModelFactory
import com.robotbot.financetracker.presentation.category.manage.ManageCategoryActivity
import com.robotbot.financetracker.presentation.category.manage.ManageCategoryActivity.Companion
import com.robotbot.financetracker.presentation.category.manage.ManageCategoryFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class ManageBankAccountActivity : AppCompatActivity(), ManageBankAccountFragment.OnWorkEndedListener {

    private val binding by lazy {
        ActivityManageBankAccountBinding.inflate(layoutInflater)
    }

    private val component by lazy {
        (application as FinanceTrackerApp).component
    }

    private var screenMode: String = MODE_UNDEFINED

    private var accountId: Int = DomainConstants.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        parseParams()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (savedInstanceState == null) {
            val fragment = when (screenMode) {
                MODE_ADD -> {
                    ManageBankAccountFragment.newInstanceAddAccount()
                }

                MODE_EDIT -> {
                    ManageBankAccountFragment.newInstanceEditAccount(accountId)
                }

                else -> {
                    throw RuntimeException("Unknown screen mode")
                }
            }
            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_content, fragment)
                .commit()
        }
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

    companion object {

        private const val EXTRA_SCREEN_MODE = "screen_mode"
        private const val MODE_ADD = "add_mode"
        private const val MODE_EDIT = "edit_mode"
        private const val MODE_UNDEFINED = ""
        private const val EXTRA_ACCOUNT_ID = "account_id"

        fun newIntentAddMode(context: Context): Intent {
            return Intent(context, ManageBankAccountActivity::class.java).apply {
                putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            }
        }

        fun newIntentEditMode(context: Context, accountId: Int): Intent {
            return Intent(context, ManageBankAccountActivity::class.java).apply {
                putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
                putExtra(EXTRA_ACCOUNT_ID, accountId)
            }
        }

    }

    override fun onWorkEndedListener() {
        finish()
    }
}