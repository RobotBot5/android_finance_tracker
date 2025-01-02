package com.robotbot.financetracker.domain.usecases.category

import com.robotbot.financetracker.domain.entities.CategoryEntity
import com.robotbot.financetracker.domain.repotisories.CategoryRepository
import javax.inject.Inject

class EditCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) {

    suspend operator fun invoke(category: CategoryEntity) = repository.update(category)

}