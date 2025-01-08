package com.robotbot.financetracker.data.repository

import com.robotbot.financetracker.domain.DomainConstants
import com.robotbot.financetracker.domain.entities.BankAccountEntity
import com.robotbot.financetracker.domain.entities.Currency
import com.robotbot.financetracker.domain.repotisories.BankAccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import java.math.BigDecimal

object BankAccountMockRepository : BankAccountRepository {

    private val accounts = loadData()

    private val refreshEvents = MutableSharedFlow<Unit>()
    override suspend fun getPrices(): Map<String, String> {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: Int): BankAccountEntity {
        return accounts.find {
            it.id == id
        } ?: throw IllegalArgumentException("Account with id:$id doesn't exist")
    }

    override fun getAll(): Flow<List<BankAccountEntity>> = flow {
        emit(accounts.toList())
        refreshEvents.collect {
            emit(accounts.toList())
        }
    }

    override suspend fun create(entity: BankAccountEntity) {
        if (entity.id == DomainConstants.UNDEFINED_ID) {
            entity.id = accounts.maxOf { it.id } + 1
        }
        accounts.add(entity)
        refreshEvents.emit(Unit)
    }

    override suspend fun update(entity: BankAccountEntity) {
        val oldAccount = getById(entity.id)
        accounts.remove(oldAccount)
        accounts.add(entity)
        accounts.sortBy { it.id }
        refreshEvents.emit(Unit)
    }

    override suspend fun delete(id: Int) {
        accounts.removeIf {
            it.id == id
        }
        refreshEvents.emit(Unit)
    }

    private fun loadData(): MutableList<BankAccountEntity> {
        val bankAccountsList = mutableListOf<BankAccountEntity>()
        for (i in 1..10) {
            bankAccountsList.add(
                BankAccountEntity(
                    name = "Account №$i",
                    balance = BigDecimal(1000 + i * 100),
                    currency = Currency.USD,
                    id = i
                )
            )
        }
        return bankAccountsList
    }
}