package com.robotbot.financetracker.presentation

data class CreateCategoryState(
    val displayState: CreateCategoryDisplayState
)

sealed interface CreateCategoryDisplayState {
    data object Initial : CreateCategoryDisplayState

    data class Content(
        val nameError: String? = null
    ) : CreateCategoryDisplayState

    data object WorkEnded : CreateCategoryDisplayState
}