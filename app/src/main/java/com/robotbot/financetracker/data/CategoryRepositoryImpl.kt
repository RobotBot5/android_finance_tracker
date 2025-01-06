package com.robotbot.financetracker.data

import com.robotbot.financetracker.data.database.CategoryDao
import com.robotbot.financetracker.di.ApplicationScope
import com.robotbot.financetracker.domain.entities.CategoryEntity
import com.robotbot.financetracker.domain.repotisories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ApplicationScope
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val mapper: CategoryMapper
) : CategoryRepository {

    override suspend fun getById(id: Int): CategoryEntity {
        return mapper.mapDbModelToEntity(categoryDao.getCategoryById(id))
    }

    override fun getAll(): Flow<List<CategoryEntity>> {
        return categoryDao.getCategories().map { mapper.mapListDbModelToListEntity(it) }
    }

    override suspend fun create(entity: CategoryEntity) {
        categoryDao.addCategory(mapper.mapEntityToDbModel(entity))
    }

    override suspend fun update(entity: CategoryEntity) {
        create(entity)
    }

    override suspend fun delete(id: Int) {
        categoryDao.deleteCategoryById(id)
    }
}