package com.robotbot.financetracker.presentation.category.manage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.robotbot.financetracker.databinding.FragmentManageCategoryBinding
import com.robotbot.financetracker.domain.DomainConstants
import com.robotbot.financetracker.domain.entities.TransactionType
import com.robotbot.financetracker.presentation.FinanceTrackerApp
import com.robotbot.financetracker.presentation.ViewModelFactory
import com.robotbot.financetracker.presentation.category.manage.icons_adapter.IconAdapter
import com.robotbot.financetracker.presentation.navigation.ManageMode
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

    @Inject
    lateinit var iconsAdapter: IconAdapter

    private val args by navArgs<ManageCategoryFragmentArgs>()

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    private fun parseParams() {
        if (args.mode == ManageMode.EDIT) {
            if (args.categoryId == DomainConstants.UNDEFINED_ID) {
                throw RuntimeException("Category id param is absent")
            }
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
                                findNavController().popBackStack()
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
        when (args.mode) {
            ManageMode.ADD -> launchAddMode()
            ManageMode.EDIT -> launchEditMode()
        }
    }

    private fun launchEditMode() {
        viewModel.setupEditCategoryById(args.categoryId)
        with(binding) {
            btnDeleteCategory.visibility = VISIBLE
            btnDeleteCategory.setOnClickListener {
                lifecycleScope.launch {
                    val categoryName = viewModel.state.value.categoryToDeleteName ?: return@launch
                    findNavController().navigate(
                        ManageCategoryFragmentDirections.actionManageCategoryFragmentToDeleteCategoryDialogFragment(
                            categoryName
                        )
                    )
                    setFragmentResultListener(
                        DeleteCategoryDialogFragment.REQUEST_KEY,
                    ) { _, bundle ->
                        val confirmed = bundle.getBoolean(DeleteCategoryDialogFragment.RESULT_KEY)
                        if (confirmed) {
                            viewModel.deleteCategory()
                        }
                    }
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
}