package com.robotbot.financetracker.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.robotbot.financetracker.FinanceTrackerApp
import com.robotbot.financetracker.R
import com.robotbot.financetracker.databinding.ActivityManageCategoryBinding
import com.robotbot.financetracker.domain.DomainConstants
import com.robotbot.financetracker.domain.entities.TransactionType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class ManageCategoryActivity : AppCompatActivity(), DeleteCategoryDialogFragment.DeleteCategoryDialogListener {

    private val binding by lazy {
        ActivityManageCategoryBinding.inflate(layoutInflater)
    }

    private val component by lazy {
        (application as FinanceTrackerApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: ManageCategoryViewModel by viewModels { viewModelFactory }

    private var screenMode: String = MODE_UNDEFINED

    private var categoryId: Int = DomainConstants.UNDEFINED_ID

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
        observeViewModel()
        setListenersOnViews()
        launchRightMode()
    }

    override fun onDialogPositiveClick() {
        viewModel.deleteCategory()
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }
    }

    private fun launchEditMode() {
        viewModel.loadCategoryEntityById(categoryId)
        with (binding) {
            btnDeleteCategory.visibility = VISIBLE
            btnSaveCategory.setOnClickListener {
                val categoryType = if (rbExpense.isChecked) {
                    TransactionType.EXPENSE
                } else {
                    TransactionType.INCOME
                }
                viewModel.editCategory(
                    inputName = etCategoryName.text.toString(),
                    type = categoryType
                )
            }
            btnDeleteCategory.setOnClickListener {
                lifecycleScope.launch {
                    val categoryName = viewModel.state.first().categoryToDeleteName ?: return@launch
                    DeleteCategoryDialogFragment.newInstance(
                        categoryName = categoryName
                    ).show(supportFragmentManager, "DELETE_CATEGORY_DIALOG")
                }
            }
        }
    }

    private fun launchAddMode() {
        with (binding) {
            btnSaveCategory.setOnClickListener {
                val categoryType = if (rbExpense.isChecked) {
                    TransactionType.EXPENSE
                } else {
                    TransactionType.INCOME
                }
                viewModel.addCategory(
                    inputName = etCategoryName.text.toString(),
                    type = categoryType
                )
            }
        }
    }

    private fun parseParams() {
        screenMode = intent.getStringExtra(EXTRA_SCREEN_MODE)
            ?: throw RuntimeException("Extra screen mode is absent")
        if (screenMode != MODE_EDIT && screenMode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $screenMode")
        }
        if (screenMode == MODE_EDIT) {
            categoryId = intent.getIntExtra(EXTRA_ACCOUNT_ID, 0)
            if (categoryId == 0) {
                throw RuntimeException("Param account id is absent")
            }
        }
    }

    private fun setListenersOnViews() {
        with(binding) {
            etCategoryName.doOnTextChanged { _, _, _, _ ->
                viewModel.resetErrorInputName()
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.state.collect {
                with (binding) {
                    when (it.displayState) {
                        is ManageCategoryDisplayState.Initial -> {
                            rbExpense.isChecked = true
                        }

                        is ManageCategoryDisplayState.Content -> {
                            tilCategoryName.error = it.displayState.nameError
                        }
                        ManageCategoryDisplayState.WorkEnded -> finish()
                        is ManageCategoryDisplayState.InitialEditMode -> {
                            etCategoryName.setText(it.displayState.categoryEntity.name)
                            if (it.displayState.categoryEntity.transactionType == TransactionType.INCOME) {
                                rbIncome.isChecked = true
                            }
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
            return Intent(context, ManageCategoryActivity::class.java).apply {
                putExtra(
                    EXTRA_SCREEN_MODE,
                    MODE_ADD
                )
            }
        }

        fun newIntentEditMode(context: Context, accountId: Int): Intent {
            return Intent(context, ManageCategoryActivity::class.java).apply {
                putExtra(
                    EXTRA_SCREEN_MODE,
                    MODE_EDIT
                )
                putExtra(EXTRA_ACCOUNT_ID, accountId)
            }
        }
    }
}