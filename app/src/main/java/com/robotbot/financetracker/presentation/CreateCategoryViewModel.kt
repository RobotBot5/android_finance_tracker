package com.robotbot.financetracker.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotbot.financetracker.domain.entities.TransactionCategoryEntity
import com.robotbot.financetracker.domain.entities.TransactionType
import com.robotbot.financetracker.domain.usecases.category.AddCategoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateCategoryViewModel @Inject constructor(
    private val addCategoryUseCase: AddCategoryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CreateCategoryState(CreateCategoryDisplayState.Initial))
    val state = _state.asStateFlow()

    fun saveCategory(
        name: String,
        type: TransactionType
    ) {
        val parsedName = name.trim()
        if (parsedName.isBlank()) {
            _state.update {
                it.copy(displayState = CreateCategoryDisplayState.Content(nameError = "Incorrect name"))
            }
            return
        }
        viewModelScope.launch {
            addCategoryUseCase(
                TransactionCategoryEntity(
                    name = parsedName,
                    transactionType = type
                )
            )
            _state.update {
                it.copy(displayState = CreateCategoryDisplayState.WorkEnded)
            }
        }
    }

    fun resetErrorInputName() {
        _state.update {
            it.copy(
                displayState = CreateCategoryDisplayState.Content(nameError = null)
            )
        }
    }
}