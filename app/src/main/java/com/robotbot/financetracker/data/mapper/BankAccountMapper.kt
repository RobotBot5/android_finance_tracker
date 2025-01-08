package com.robotbot.financetracker.data.mapper

import com.robotbot.financetracker.data.database.model.BankAccountDbModel
import com.robotbot.financetracker.domain.entities.BankAccountEntity
import javax.inject.Inject

class BankAccountMapper @Inject constructor() {

    fun mapEntityToDbModel(bankAccountEntity: BankAccountEntity): BankAccountDbModel = BankAccountDbModel(
        id = bankAccountEntity.id,
        name = bankAccountEntity.name,
        balance = bankAccountEntity.balance,
        currency = bankAccountEntity.currency
    )

    fun mapDbModelToEntity(bankAccountDbModel: BankAccountDbModel): BankAccountEntity = BankAccountEntity(
        id = bankAccountDbModel.id,
        name = bankAccountDbModel.name,
        balance = bankAccountDbModel.balance,
        currency = bankAccountDbModel.currency
    )

    fun mapListDbModelToListEntity(list: List<BankAccountDbModel>): List<BankAccountEntity> = list.map {
        mapDbModelToEntity(it)
    }

}