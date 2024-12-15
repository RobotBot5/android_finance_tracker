package com.robotbot.financetracker.domain.repotisories

interface CrudRepository<T> {

    fun getById(id: Int): T

    fun getAll(): List<T>

    fun create(entity: T)

    fun update(entity: T)

    fun delete(id: Int)

}