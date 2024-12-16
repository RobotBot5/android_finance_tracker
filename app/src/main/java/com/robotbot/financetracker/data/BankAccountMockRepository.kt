package com.robotbot.financetracker.data

import com.robotbot.financetracker.domain.entities.BankAccountEntity
import com.robotbot.financetracker.domain.entities.Currency
import com.robotbot.financetracker.domain.repotisories.BankAccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.math.BigDecimal

object BankAccountMockRepository : BankAccountRepository {

    private val accounts = loadData()

    override fun getById(id: Int): BankAccountEntity {
        TODO("Not yet implemented")
    }

    override fun getAll(): Flow<List<BankAccountEntity>> = flow {
        emit(accounts.toList())
    }

    override fun create(entity: BankAccountEntity) {
        TODO("Not yet implemented")
    }

    override fun update(entity: BankAccountEntity) {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int) {
        TODO("Not yet implemented")
    }

    private fun loadData(): MutableList<BankAccountEntity> {
        val bankAccountsList = mutableListOf<BankAccountEntity>()
        repeat(10) {
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