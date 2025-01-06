package com.robotbot.financetracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.robotbot.financetracker.data.database.model.CategoryDbModel
import com.robotbot.financetracker.data.database.model.Converters

@Database(entities = [CategoryDbModel::class], version = 1, exportSchema = false)
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
                    ).build()
                return instance.also {
                    db = it
                }
            }
        }
    }

    abstract fun categoryDao(): CategoryDao

}