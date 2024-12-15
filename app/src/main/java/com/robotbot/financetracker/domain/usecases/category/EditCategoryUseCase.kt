package com.robotbot.financetracker.domain.usecases.category

import com.robotbot.financetracker.domain.entities.TransactionCategoryEntity
import com.robotbot.financetracker.domain.repotisories.CategoryRepository

class EditCategoryUseCase(
    private val repository: CategoryRepository
) {

    operator fun invoke(category: TransactionCategoryEntity) = repository.update(category)

}