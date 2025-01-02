package com.robotbot.financetracker.presentation.category.manage

import android.app.Application
import androidx.core.content.res.use
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotbot.financetracker.R
import com.robotbot.financetracker.domain.DomainConstants
import com.robotbot.financetracker.domain.entities.CategoryEntity
import com.robotbot.financetracker.domain.entities.TransactionType
import com.robotbot.financetracker.domain.usecases.category.AddCategoryUseCase
import com.robotbot.financetracker.domain.usecases.category.DeleteCategoryUseCase
import com.robotbot.financetracker.domain.usecases.category.EditCategoryUseCase
import com.robotbot.financetracker.domain.usecases.category.GetCategoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ManageCategoryViewModel @Inject constructor(
    private val addCategoryUseCase: AddCategoryUseCase,
    private val editCategoryUseCase: EditCategoryUseCase,
    private val getCategoryUseCase: GetCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val application: Application
) : ViewModel() {

    private val _state = MutableStateFlow(
        ManageCategoryState(
            displayState = ManageCategoryDisplayState.Initial
        )
    )
    val state = _state.asStateFlow()

    private var editCategoryIdOrUndefined: Int = DomainConstants.UNDEFINED_ID

    fun editCategory(inputName: String, type: TransactionType) {
        handleCategoryOperation(inputName, type) { category ->
            editCategoryUseCase(category)
        }
    }

    fun addCategory(inputName: String, type: TransactionType) {
        handleCategoryOperation(inputName, type) { category ->
            addCategoryUseCase(category)
        }
    }

    fun deleteCategory() {
        viewModelScope.launch {
            deleteCategoryUseCase(editCategoryIdOrUndefined)
            _state.update {
                it.copy(
                    displayState = ManageCategoryDisplayState.WorkEnded
                )
            }
        }
    }

    private fun handleCategoryOperation(
        inputName: String,
        type: TransactionType,
        operation: suspend (CategoryEntity) -> Unit
    ) {
        val name = inputName.trim()

        val errorByInputName = validateInputName(name)
        if (errorByInputName != null) {
            _state.update {
                it.copy(displayState = ManageCategoryDisplayState.Content(nameError = errorByInputName))
            }
            return
        }

        viewModelScope.launch {
            operation(
                CategoryEntity(
                    id = editCategoryIdOrUndefined,
                    name = name,
                    transactionType = type,
                    iconResId = R.drawable.ic_category_shopping_cart
                )
            )
            _state.update {
                it.copy(displayState = ManageCategoryDisplayState.WorkEnded)
            }
        }
    }

    private fun validateInputName(name: String): String? {
        return if (name.isBlank()) {
            application.getString(R.string.til_category_error_name)
        } else null
    }

    fun resetErrorInputName() {
        _state.update {
            it.copy(
                displayState = ManageCategoryDisplayState.Content(nameError = null)
            )
        }
    }

    fun setupEditCategoryById(categoryId: Int) {
        viewModelScope.launch {
            val categoryEntity = getCategoryUseCase(categoryId)
            editCategoryIdOrUndefined = categoryEntity.id
            _state.update {
                it.copy(
                    displayState = ManageCategoryDisplayState.InitialEditMode(
                        categoryEntity
                    ),
                    categoryToDeleteName = categoryEntity.name
                )
            }
        }
    }
}