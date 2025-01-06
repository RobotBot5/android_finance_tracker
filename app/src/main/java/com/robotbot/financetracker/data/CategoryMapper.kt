package com.robotbot.financetracker.data

import com.robotbot.financetracker.data.database.model.CategoryDbModel
import com.robotbot.financetracker.domain.entities.CategoryEntity
import javax.inject.Inject

class CategoryMapper @Inject constructor() {

    fun mapEntityToDbModel(categoryEntity: CategoryEntity): CategoryDbModel = CategoryDbModel(
        id = categoryEntity.id,
        transactionType = categoryEntity.transactionType,
        name = categoryEntity.name,
        iconResName = categoryEntity.iconResName
    )

    fun mapDbModelToEntity(categoryDbModel: CategoryDbModel): CategoryEntity = CategoryEntity(
        id = categoryDbModel.id,
        transactionType = categoryDbModel.transactionType,
        name = categoryDbModel.name,
        iconResName = categoryDbModel.iconResName
    )

    fun mapListDbModelToListEntity(list: List<CategoryDbModel>): List<CategoryEntity> = list.map {
        mapDbModelToEntity(it)
    }

}