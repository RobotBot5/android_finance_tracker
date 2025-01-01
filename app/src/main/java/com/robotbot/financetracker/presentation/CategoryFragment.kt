package com.robotbot.financetracker.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.robotbot.financetracker.FinanceTrackerApp
import com.robotbot.financetracker.databinding.FragmentCategoryBinding
import com.robotbot.financetracker.presentation.category_adapter.CategoryAdapter
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

    @Inject
    lateinit var categoryAdapter: CategoryAdapter

    private lateinit var viewModel: CategoryViewModel

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
        categoryAdapter.onAddButtonClickListener = {
            ManageCategoryActivity.newIntentAddMode(requireContext()).apply {
                startActivity(this)
            }
        }
        categoryAdapter.onCategoryClickListener = {
            ManageCategoryActivity.newIntentEditMode(requireContext(), it.id).apply {
                startActivity(this)
            }
        }
        binding.rvCategories.adapter = categoryAdapter
        lifecycleScope.launch {
            viewModel.categories.collect {
                categoryAdapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance(): CategoryFragment {
            return CategoryFragment()
        }
    }
}