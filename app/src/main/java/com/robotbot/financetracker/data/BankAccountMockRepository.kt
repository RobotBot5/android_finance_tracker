package com.robotbot.financetracker.data

import com.robotbot.financetracker.domain.entities.BankAccountEntity
import com.robotbot.financetracker.domain.entities.Currency
import com.robotbot.financetracker.domain.repotisories.BankAccountRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import java.math.BigDecimal

object BankAccountMockRepository : BankAccountRepository {

    private val accounts = loadData()

    private val refreshEvents = MutableSharedFlow<Unit>()

    override fun getById(id: Int): BankAccountEntity {
        TODO("Not yet implemented")
    }

    override fun getAll(): Flow<List<BankAccountEntity>> = flow {
        emit(accounts.toList())
        refreshEvents.collect {
            emit(accounts.toList())
        }
    }

    override suspend fun create(entity: BankAccountEntity) {
        accounts.add(entity)
        refreshEvents.emit(Unit)
    }

    override fun update(entity: BankAccountEntity) {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int) {
        TODO("Not yet implemented")
    }

    private fun loadData(): MutableList<BankAccountEntity> {
        val bankAccountsList = mutableListOf<BankAccountEntity>()
        repeat(50) {
            bankAccountsList.add(
                BankAccountEntity(
                    name = "Account №$it",
                    balance = BigDecimal(1000 + it * 100),
                    currency = Currency.USD,
                    id = it
                )
            )
        }
        return bankAccountsList
    }
}