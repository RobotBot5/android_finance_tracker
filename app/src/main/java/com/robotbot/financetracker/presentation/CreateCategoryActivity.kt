package com.robotbot.financetracker.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.robotbot.financetracker.FinanceTrackerApp
import com.robotbot.financetracker.R
import com.robotbot.financetracker.databinding.ActivityCreateCategoryBinding
import com.robotbot.financetracker.domain.entities.TransactionType
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateCategoryActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCreateCategoryBinding.inflate(layoutInflater)
    }

    private val component by lazy {
        (application as FinanceTrackerApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: CreateCategoryViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
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
    }

    private fun setListenersOnViews() {
        with(binding) {
            etCategoryName.doOnTextChanged { _, _, _, _ ->
                viewModel.resetErrorInputName()
            }
            btnSaveCategory.setOnClickListener {
                val categoryType = if (rbExpense.isChecked) {
                    TransactionType.EXPENSE
                } else {
                    TransactionType.INCOME
                }
                viewModel.saveCategory(
                    name = etCategoryName.text.toString(),
                    type = categoryType
                )
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.state.collect {
                with (binding) {
                    when (it.displayState) {
                        is CreateCategoryDisplayState.Initial -> {
                            rbExpense.isChecked = true
                        }

                        is CreateCategoryDisplayState.Content -> {
                            tilCategoryName.error = it.displayState.nameError
                        }
                        CreateCategoryDisplayState.WorkEnded -> finish()
                    }
                }
            }
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, CreateCategoryActivity::class.java)
    }
}