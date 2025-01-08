package com.robotbot.financetracker.data.repository

import com.robotbot.financetracker.data.database.dao.BankAccountDao
import com.robotbot.financetracker.data.mapper.BankAccountMapper
import com.robotbot.financetracker.di.ApplicationScope
import com.robotbot.financetracker.domain.entities.BankAccountEntity
import com.robotbot.financetracker.domain.repotisories.BankAccountRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@ApplicationScope
class BankAccountRepositoryImpl @Inject constructor(
    private val bankAccountDao: BankAccountDao,
    private val mapper: BankAccountMapper
) : BankAccountRepository {

    override suspend fun getById(id: Int): BankAccountEntity {
        return mapper.mapDbModelToEntity(bankAccountDao.getAccountById(id))
    }

    override fun getAll(): Flow<List<BankAccountEntity>> {
        return bankAccountDao.getAccounts()
            .map { mapper.mapListDbModelToListEntity(it) }
    }

    override suspend fun create(entity: BankAccountEntity) {
        bankAccountDao.addAccount(mapper.mapEntityToDbModel(entity))
    }

    override suspend fun update(entity: BankAccountEntity) {
        create(entity)
    }

    override suspend fun delete(id: Int) {
        bankAccountDao.deleteAccountById(id)
    }
}