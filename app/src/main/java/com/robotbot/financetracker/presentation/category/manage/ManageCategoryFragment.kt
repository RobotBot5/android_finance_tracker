package com.robotbot.financetracker.presentation.category.manage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.robotbot.financetracker.databinding.FragmentManageCategoryBinding
import com.robotbot.financetracker.domain.DomainConstants
import com.robotbot.financetracker.domain.entities.TransactionType
import com.robotbot.financetracker.presentation.FinanceTrackerApp
import com.robotbot.financetracker.presentation.ViewModelFactory
import com.robotbot.financetracker.presentation.category.manage.icons_adapter.IconAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

class ManageCategoryFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as FinanceTrackerApp).component
    }

    private var _binding: FragmentManageCategoryBinding? = null
    private val binding: FragmentManageCategoryBinding
        get() = _binding ?: throw RuntimeException("FragmentManageCategoryBinding == null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: ManageCategoryViewModel

    private lateinit var onWorkEndedListener: OnWorkEndedListener

    interface OnWorkEndedListener {
        fun onWorkEndedListener()
    }

    @Inject
    lateinit var iconsAdapter: IconAdapter

    private var screenMode: String = MODE_UNDEFINED

    private var categoryId: Int = DomainConstants.UNDEFINED_ID

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
            if (!args.containsKey(CATEGORY_ID)) {
                throw RuntimeException("Category id param is absent")
            }
            categoryId = args.getInt(CATEGORY_ID, DomainConstants.UNDEFINED_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[ManageCategoryViewModel::class.java]
        launchRightMode()
        observeViewModel()
        setListenersOnViews()
        iconsAdapter.onCategoryIconClickListener = {
            viewModel.setSelectedCategoryIcon(it)
        }
        binding.rvCategoryIcons.adapter = iconsAdapter
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect {
                    iconsAdapter.submitList(
                        it.iconResIds
                    )
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

                            ManageCategoryDisplayState.WorkEnded -> {
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
            etCategoryName.doOnTextChanged { _, _, _, _ ->
                viewModel.resetErrorInputName()
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
        viewModel.setupEditCategoryById(categoryId)
        with(binding) {
            btnDeleteCategory.visibility = VISIBLE
            btnDeleteCategory.setOnClickListener {
                lifecycleScope.launch {
                    val categoryName = viewModel.state.value.categoryToDeleteName ?: return@launch
                    parentFragmentManager.setFragmentResultListener(
                        DeleteCategoryDialogFragment.REQUEST_KEY,
                        viewLifecycleOwner
                    ) { _, bundle ->
                        val confirmed = bundle.getBoolean(DeleteCategoryDialogFragment.RESULT_KEY)
                        if (confirmed) {
                            viewModel.deleteCategory()
                        }
                    }
                    DeleteCategoryDialogFragment.newInstance(
                        categoryName = categoryName
                    ).show(parentFragmentManager, "DELETE_CATEGORY_DIALOG")
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val SCREEN_MODE = "screen_mode"
        private const val MODE_ADD = "add_mode"
        private const val MODE_EDIT = "edit_mode"
        private const val MODE_UNDEFINED = ""
        private const val CATEGORY_ID = "category_id"

        fun newInstanceAddCategory(): ManageCategoryFragment {
            return ManageCategoryFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditCategory(categoryId: Int): ManageCategoryFragment {
            return ManageCategoryFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(CATEGORY_ID, categoryId)
                }
            }
        }
    }
}