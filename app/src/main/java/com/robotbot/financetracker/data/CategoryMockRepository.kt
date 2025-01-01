package com.robotbot.financetracker.data

import android.util.Log
import com.robotbot.financetracker.domain.DomainConstants
import com.robotbot.financetracker.domain.entities.TransactionCategoryEntity
import com.robotbot.financetracker.domain.entities.TransactionType
import com.robotbot.financetracker.domain.repotisories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow

object CategoryMockRepository : CategoryRepository {

    private val categories = loadData()

    private val refreshEvents = MutableSharedFlow<Unit>()

    override suspend fun getById(id: Int): TransactionCategoryEntity {
        return categories.find {
            it.id == id
        } ?: throw IllegalArgumentException("Category with id:$id doesn't exist")
    }

    override fun getAll(): Flow<List<TransactionCategoryEntity>> = flow {
        emit(categories.toList())
        refreshEvents.collect {
            emit(categories.toList())
        }
    }

    override suspend fun create(entity: TransactionCategoryEntity) {
        if (entity.id == DomainConstants.UNDEFINED_ID) {
            entity.id = categories.maxOf { it.id } + 1
        }
        categories.add(entity)
        refreshEvents.emit(Unit)
    }

    override suspend fun update(entity: TransactionCategoryEntity) {
        val oldCategory = getById(entity.id)
        categories.remove(oldCategory)
        categories.add(entity)
        categories.sortBy { it.id }
        Log.d("CategoryMockRepository", "Start")
        categories.forEach {
            Log.d("CategoryMockRepository", it.toString())
        }
        Log.d("CategoryMockRepository", "End")
        refreshEvents.emit(Unit)
    }

    override suspend fun delete(id: Int) {
        categories.removeIf {
            it.id == id
        }
        refreshEvents.emit(Unit)
    }

    private fun loadData(): MutableList<TransactionCategoryEntity> {
        val categoriesList = mutableListOf<TransactionCategoryEntity>()
        for (i in 1..10) {
            categoriesList.add(
                TransactionCategoryEntity(
                    id = i,
                    transactionType = TransactionType.INCOME,
                    name = "Name: $i"
                )
            )
        }
        return categoriesList
    }
}