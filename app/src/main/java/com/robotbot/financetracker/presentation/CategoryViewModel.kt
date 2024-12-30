package com.robotbot.financetracker.presentation

import androidx.lifecycle.ViewModel
import com.robotbot.financetracker.domain.usecases.category.GetCategoryListUseCase
import javax.inject.Inject

class CategoryViewModel @Inject constructor(
    getCategoryListUseCase: GetCategoryListUseCase
) : ViewModel() {

    val categories = getCategoryListUseCase()

}