package com.robotbot.financetracker.domain.usecases.category

import com.robotbot.financetracker.di.RealCategoryDatabaseQualifier
import com.robotbot.financetracker.domain.repotisories.CategoryRepository
import javax.inject.Inject

class GetCategoryUseCase @Inject constructor(
    @RealCategoryDatabaseQualifier private val repository: CategoryRepository
) {

    suspend operator fun invoke(id: Int) = repository.getById(id)

}