package com.robotbot.financetracker.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.robotbot.financetracker.data.database.model.CategoryDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    fun getCategories(): Flow<List<CategoryDbModel>>

    @Query("SELECT * FROM categories WHERE id=:id LIMIT 1")
    suspend fun getCategoryById(id: Int): CategoryDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategory(categoryDbModel: CategoryDbModel)

    @Query("DELETE FROM categories WHERE id=:id")
    suspend fun deleteCategoryById(id: Int)

}