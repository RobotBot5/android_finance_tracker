package com.robotbot.financetracker.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotbot.financetracker.domain.usecases.category.GetCategoryListUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class CategoryViewModel @Inject constructor(
    getCategoryListUseCase: GetCategoryListUseCase
) : ViewModel() {

    val state = getCategoryListUseCase()
        .map { CategoryState(displayState = CategoryDisplayState.Content(it)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = CategoryState(displayState = CategoryDisplayState.Loading)
        )

}