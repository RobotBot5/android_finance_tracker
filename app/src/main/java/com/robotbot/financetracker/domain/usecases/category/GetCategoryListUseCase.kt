package com.robotbot.financetracker.domain.usecases.category

import com.robotbot.financetracker.domain.repotisories.CategoryRepository

class GetCategoryListUseCase(
    private val repository: CategoryRepository
) {

    operator fun invoke() = repository.getAll()

}