package com.robotbot.financetracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.robotbot.financetracker.data.database.dao.BankAccountDao
import com.robotbot.financetracker.data.database.dao.CategoryDao
import com.robotbot.financetracker.data.database.dao.TransferDao
import com.robotbot.financetracker.data.database.model.BankAccountDbModel
import com.robotbot.financetracker.data.database.model.CategoryDbModel
import com.robotbot.financetracker.data.database.model.Converters
import com.robotbot.financetracker.data.database.model.TransferDbModel

@Database(
    entities = [
        CategoryDbModel::class,
        BankAccountDbModel::class,
        TransferDbModel::class
    ], version = 1, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private var db: AppDatabase? = null
        private const val DB_NAME = "main.db"
        private val LOCK = Any()

        fun getInstance(context: Context): AppDatabase {
            db?.let { return it }
            synchronized(LOCK) {
                db?.let { return it }
                val instance =
                    Room.databaseBuilder(
                        context = context,
                        klass = AppDatabase::class.java,
                        name = DB_NAME
                    )
                        .createFromAsset("database/categories.db")
                        .build()
                return instance.also {
                    db = it
                }
            }
        }
    }

    abstract fun categoryDao(): CategoryDao

    abstract fun bankAccountDao(): BankAccountDao

    abstract fun transferDao(): TransferDao

}