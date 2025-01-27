package com.robotbot.financetracker.data.repository

import com.robotbot.financetracker.data.database.dao.TransferDao
import com.robotbot.financetracker.data.mapper.TransferMapper
import com.robotbot.financetracker.di.ApplicationScope
import com.robotbot.financetracker.domain.entities.TransferEntity
import com.robotbot.financetracker.domain.repotisories.TransferRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@ApplicationScope
class TransferRepositoryImpl @Inject constructor(
    private val transferDao: TransferDao,
    private val mapper: TransferMapper
) : TransferRepository {

    override suspend fun getById(id: Int): TransferEntity {
        return mapper.mapDbModelToEntity(transferDao.getTransferById(id))
    }

    override fun getAll(): Flow<List<TransferEntity>> {
        return transferDao.getTransfers()
            .onEach { delay(3000) }
            .map { mapper.mapListDbModelToListEntity(it) }
    }

    override suspend fun create(entity: TransferEntity) {
        transferDao.addTransfer(mapper.mapEntityToDbModel(entity))
    }

    override suspend fun update(entity: TransferEntity) {
        create(entity)
    }

    override suspend fun delete(id: Int) {
        transferDao.deleteTransferById(id)
    }
}