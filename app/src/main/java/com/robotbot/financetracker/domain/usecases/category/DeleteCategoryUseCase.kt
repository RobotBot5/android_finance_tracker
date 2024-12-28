package com.robotbot.financetracker.domain.usecases.category

import com.robotbot.financetracker.domain.repotisories.CategoryRepository

class DeleteCategoryUseCase(
    private val repository: CategoryRepository
) {

    suspend operator fun invoke(id: Int) = repository.delete(id)

}