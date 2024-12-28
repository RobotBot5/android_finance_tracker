package com.robotbot.financetracker.domain.repotisories

import kotlinx.coroutines.flow.Flow

interface CrudRepository<T> {

    suspend fun getById(id: Int): T

    fun getAll(): Flow<List<T>>

    suspend fun create(entity: T)

    suspend fun update(entity: T)

    suspend fun delete(id: Int)

}