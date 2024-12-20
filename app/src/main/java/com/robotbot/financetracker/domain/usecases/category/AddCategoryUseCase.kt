package com.robotbot.financetracker.domain.usecases.category

import com.robotbot.financetracker.domain.entities.TransactionCategoryEntity
import com.robotbot.financetracker.domain.repotisories.CategoryRepository

class AddCategoryUseCase(
    private val repository: CategoryRepository
) {

    suspend operator fun invoke(category: TransactionCategoryEntity) = repository.create(category)

}