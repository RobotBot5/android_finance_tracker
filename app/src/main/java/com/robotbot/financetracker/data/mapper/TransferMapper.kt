package com.robotbot.financetracker.data.mapper

import com.robotbot.financetracker.data.database.model.TransferDbModel
import com.robotbot.financetracker.data.database.model.TransferWithAccountsDbModel
import com.robotbot.financetracker.domain.entities.TransferEntity
import javax.inject.Inject

class TransferMapper @Inject constructor(
    private val bankAccountMapper: BankAccountMapper
) {

    fun mapEntityToDbModel(transferEntity: TransferEntity): TransferDbModel = TransferDbModel(
        id = transferEntity.id,
        fromAccountId = transferEntity.accountFrom.id,
        toAccountId = transferEntity.accountTo.id,
        amount = transferEntity.amount
    )

    fun mapDbModelToEntity(transferWithAccountsDbModel: TransferWithAccountsDbModel): TransferEntity = TransferEntity(
        id = transferWithAccountsDbModel.transfer.id,
        accountFrom = bankAccountMapper.mapDbModelToEntity(transferWithAccountsDbModel.fromBankAccount),
        accountTo = bankAccountMapper.mapDbModelToEntity(transferWithAccountsDbModel.toBankAccount),
        amount = transferWithAccountsDbModel.transfer.amount
    )

    fun mapListDbModelToListEntity(list: List<TransferWithAccountsDbModel>): List<TransferEntity> = list.map {
        mapDbModelToEntity(it)
    }
}