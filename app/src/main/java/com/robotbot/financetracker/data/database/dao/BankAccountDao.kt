package com.robotbot.financetracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.robotbot.financetracker.data.database.model.BankAccountDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface BankAccountDao {

    @Query("SELECT * FROM bank_accounts")
    fun getAccounts(): Flow<List<BankAccountDbModel>>

    @Query("SELECT * FROM bank_accounts WHERE id=:id LIMIT 1")
    suspend fun getAccountById(id: Int): BankAccountDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAccount(bankAccountDbModel: BankAccountDbModel)

    @Query("DELETE FROM bank_accounts WHERE id=:id")
    suspend fun deleteAccountById(id: Int)

}