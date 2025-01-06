package com.robotbot.financetracker.domain.usecases.category

import com.robotbot.financetracker.di.RealCategoryDatabaseQualifier
import com.robotbot.financetracker.domain.entities.CategoryEntity
import com.robotbot.financetracker.domain.repotisories.CategoryRepository
import javax.inject.Inject

class AddCategoryUseCase @Inject constructor(
    @RealCategoryDatabaseQualifier private val repository: CategoryRepository
) {

    suspend operator fun invoke(category: CategoryEntity) = repository.create(category)

}