package com.robotbot.financetracker.presentation.category.manage

import com.robotbot.financetracker.domain.entities.CategoryEntity

data class ManageCategoryState(
    val displayState: ManageCategoryDisplayState,
    val categoryToDeleteName: String? = null
)

sealed interface ManageCategoryDisplayState {
    data object Initial : ManageCategoryDisplayState

    data class InitialEditMode(val categoryEntity: CategoryEntity) :
        ManageCategoryDisplayState

    data class Content(
        val nameError: String? = null
    ) : ManageCategoryDisplayState

    data object WorkEnded : ManageCategoryDisplayState
}