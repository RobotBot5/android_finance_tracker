package com.robotbot.financetracker.presentation.category

import com.robotbot.financetracker.domain.entities.CategoryEntity

data class CategoryState(
    val displayState: CategoryDisplayState
)

sealed interface CategoryDisplayState {
    data object Initial : CategoryDisplayState
    data object Loading : CategoryDisplayState
    data class Content(val categories: List<CategoryEntity>) : CategoryDisplayState
}