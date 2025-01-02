package com.robotbot.financetracker.presentation.category.manage

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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.robotbot.financetracker.presentation.FinanceTrackerApp
import com.robotbot.financetracker.R
import com.robotbot.financetracker.databinding.ActivityManageCategoryBinding
import com.robotbot.financetracker.domain.DomainConstants
import com.robotbot.financetracker.domain.entities.TransactionType
import com.robotbot.financetracker.presentation.ViewModelFactory
import com.robotbot.financetracker.presentation.category.manage.icons_adapter.IconAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class ManageCategoryActivity : AppCompatActivity(),
    DeleteCategoryDialogFragment.DeleteCategoryDialogListener {

    private val binding by lazy {
        ActivityManageCategoryBinding.inflate(layoutInflater)
    }

    private val component by lazy {
        (application as FinanceTrackerApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: ManageCategoryViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var iconsAdapter: IconAdapter

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
        binding.rvCategoryIcons.adapter = iconsAdapter
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

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect {
                    with(binding) {
                        when (it.displayState) {
                            is ManageCategoryDisplayState.Initial -> {
                                rbExpense.isChecked = true
                            }

                            is ManageCategoryDisplayState.InitialEditMode -> {
                                etCategoryName.setText(it.displayState.categoryEntity.name)
                                if (it.displayState.categoryEntity.transactionType == TransactionType.INCOME) {
                                    rbIncome.isChecked = true
                                }
                            }

                            is ManageCategoryDisplayState.Content -> {
                                tilCategoryName.error = it.displayState.nameError
                            }

                            ManageCategoryDisplayState.WorkEnded -> finish()
                        }
                    }
                }
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

    private fun launchRightMode() {
        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }
    }

    private fun launchEditMode() {
        viewModel.setupEditCategoryById(categoryId)
        with(binding) {
            btnDeleteCategory.visibility = VISIBLE
            btnDeleteCategory.setOnClickListener {
                lifecycleScope.launch {
                    val categoryName = viewModel.state.value.categoryToDeleteName ?: return@launch
                    DeleteCategoryDialogFragment.newInstance(
                        categoryName = categoryName
                    ).show(supportFragmentManager, "DELETE_CATEGORY_DIALOG")
                }
            }
            btnSaveCategory.setOnClickListener {
                val categoryType = getCategoryType()
                viewModel.editCategory(
                    inputName = etCategoryName.text.toString(),
                    type = categoryType
                )
            }
        }
    }

    private fun launchAddMode() {
        with(binding) {
            btnSaveCategory.setOnClickListener {
                val categoryType = getCategoryType()
                viewModel.addCategory(
                    inputName = etCategoryName.text.toString(),
                    type = categoryType
                )
            }
        }
    }

    private fun getCategoryType(): TransactionType {
        return if (binding.rbExpense.isChecked) {
            TransactionType.EXPENSE
        } else {
            TransactionType.INCOME
        }
    }

    override fun onDeleteCategoryDialogPositiveClick() {
        viewModel.deleteCategory()
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