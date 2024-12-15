package com.robotbot.financetracker.domain.usecases.category

import com.robotbot.financetracker.domain.repotisories.CategoryRepository

class GetCategoryUseCase(
    private val repository: CategoryRepository
) {

    operator fun invoke(id: Int) = repository.getById(id)

}