package com.robotbot.financetracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.robotbot.financetracker.data.database.model.TransferDbModel
import com.robotbot.financetracker.data.database.model.TransferWithAccountsDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TransferDao {

    @Transaction
    @Query("SELECT * FROM transfers")
    fun getTransfers(): Flow<List<TransferWithAccountsDbModel>>

    @Transaction
    @Query("SELECT * FROM transfers WHERE id=:id LIMIT 1")
    suspend fun getTransferById(id: Int): TransferWithAccountsDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTransfer(transferDbModel: TransferDbModel)

    @Query("DELETE FROM transfers WHERE id=:id")
    suspend fun deleteTransferById(id: Int)

}