package com.robotbot.financetracker.presentation.category

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.robotbot.financetracker.presentation.FinanceTrackerApp
import com.robotbot.financetracker.databinding.FragmentCategoryBinding
import com.robotbot.financetracker.presentation.ViewModelFactory
import com.robotbot.financetracker.presentation.category.category_adapter.CategoryAdapter
import com.robotbot.financetracker.presentation.navigation.ManageMode
import kotlinx.coroutines.launch
import javax.inject.Inject

class CategoryFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as FinanceTrackerApp).component
    }

    private var _binding: FragmentCategoryBinding? = null
    private val binding: FragmentCategoryBinding
        get() = _binding ?: throw RuntimeException("FragmentCategoryBinding == null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: CategoryViewModel

    @Inject
    lateinit var categoryAdapter: CategoryAdapter

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[CategoryViewModel::class.java]
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        categoryAdapter.onAddButtonClickListener = {
            findNavController().navigate(
                CategoryFragmentDirections.actionCategoryFragmentToManageCategoryFragment(
                    ManageMode.ADD
                )
            )
        }
        categoryAdapter.onCategoryClickListener = {
            findNavController().navigate(
                CategoryFragmentDirections.actionCategoryFragmentToManageCategoryFragment(
                    ManageMode.EDIT
                ).apply {
                    categoryId = it.id
                }
            )
        }
        binding.rvCategories.adapter = categoryAdapter
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect {
                    binding.pbCategories.visibility = GONE
                    binding.rvCategories.visibility = GONE
                    when (it.displayState) {
                        CategoryDisplayState.Initial -> {  }
                        CategoryDisplayState.Loading -> {
                            binding.pbCategories.visibility = VISIBLE
                        }
                        is CategoryDisplayState.Content -> {
                            categoryAdapter.submitList(it.displayState.categories)
                            binding.rvCategories.visibility = VISIBLE
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