package com.robotbot.financetracker.presentation

import com.robotbot.financetracker.domain.entities.TransactionCategoryEntity

data class ManageCategoryState(
    val displayState: ManageCategoryDisplayState,
    val categoryToDeleteName: String? = null
)

sealed interface ManageCategoryDisplayState {
    data object Initial : ManageCategoryDisplayState

    data class InitialEditMode(val categoryEntity: TransactionCategoryEntity) : ManageCategoryDisplayState

    data class Content(
        val nameError: String? = null
    ) : ManageCategoryDisplayState

    data object WorkEnded : ManageCategoryDisplayState
}