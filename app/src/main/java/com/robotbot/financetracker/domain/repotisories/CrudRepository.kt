package com.robotbot.financetracker.domain.repotisories

import kotlinx.coroutines.flow.Flow

interface CrudRepository<T> {

    fun getById(id: Int): T

    fun getAll(): Flow<List<T>>

    fun create(entity: T)

    fun update(entity: T)

    fun delete(id: Int)

}