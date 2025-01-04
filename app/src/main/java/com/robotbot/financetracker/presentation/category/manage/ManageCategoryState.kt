package com.robotbot.financetracker.presentation.category.manage

import com.robotbot.financetracker.domain.entities.CategoryEntity
import com.robotbot.financetracker.domain.entities.CategoryIconEntity

data class ManageCategoryState(
    val displayState: ManageCategoryDisplayState,
    val categoryToDeleteName: String? = null,
    val iconResIds: List<CategoryIconEntity>
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