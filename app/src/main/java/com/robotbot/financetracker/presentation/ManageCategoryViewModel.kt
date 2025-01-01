package com.robotbot.financetracker.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotbot.financetracker.domain.DomainConstants
import com.robotbot.financetracker.domain.entities.TransactionCategoryEntity
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
) : ViewModel() {

    private val _state = MutableStateFlow(ManageCategoryState(ManageCategoryDisplayState.Initial))
    val state = _state.asStateFlow()

    private var editCategoryId: Int = DomainConstants.UNDEFINED_ID

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

    private fun handleCategoryOperation(
        inputName: String,
        type: TransactionType,
        operation: suspend (TransactionCategoryEntity) -> Unit
    ) {
        val name = inputName.trim()

        if (name.isBlank()) {
            _state.update {
                it.copy(displayState = ManageCategoryDisplayState.Content(nameError = "Incorrect name"))
            }
            return
        }

        viewModelScope.launch {
            operation(
                TransactionCategoryEntity(
                    id = editCategoryId,
                    name = name,
                    transactionType = type
                )
            )
            _state.update {
                it.copy(displayState = ManageCategoryDisplayState.WorkEnded)
            }
        }
    }

    fun resetErrorInputName() {
        _state.update {
            it.copy(
                displayState = ManageCategoryDisplayState.Content(nameError = null)
            )
        }
    }

    fun deleteCategory() {
        viewModelScope.launch {
            deleteCategoryUseCase(editCategoryId)
            _state.update {
                it.copy(
                    displayState = ManageCategoryDisplayState.WorkEnded
                )
            }
        }
    }

    fun loadCategoryEntityById(categoryId: Int) {
        viewModelScope.launch {
            val categoryEntity = getCategoryUseCase(categoryId)
            editCategoryId = categoryEntity.id
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