package com.robotbot.financetracker.domain.usecases.category

import com.robotbot.financetracker.domain.entities.TransactionCategoryEntity
import com.robotbot.financetracker.domain.repotisories.CategoryRepository
import javax.inject.Inject

class EditCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) {

    suspend operator fun invoke(category: TransactionCategoryEntity) = repository.update(category)

}