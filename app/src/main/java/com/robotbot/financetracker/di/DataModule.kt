package com.robotbot.financetracker.di

import android.app.Application
import android.content.Context
import com.robotbot.financetracker.data.BankAccountMockRepository
import com.robotbot.financetracker.data.CategoryMockRepository
import com.robotbot.financetracker.data.CategoryRepositoryImpl
import com.robotbot.financetracker.data.database.AppDatabase
import com.robotbot.financetracker.data.database.CategoryDao
import com.robotbot.financetracker.domain.repotisories.BankAccountRepository
import com.robotbot.financetracker.domain.repotisories.CategoryRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    @RealCategoryDatabaseQualifier
    fun bindCategoryRepository(categoryRepositoryImpl: CategoryRepositoryImpl): CategoryRepository

    companion object {

        @Provides
        fun provideBankAccountMockRepository(): BankAccountRepository = BankAccountMockRepository

        @Provides
        @MockCategoryDatabaseQualifier
        fun provideCategoryMockRepository(): CategoryRepository = CategoryMockRepository

        @ApplicationScope
        @Provides
        fun provideCategoryDao(application: Application): CategoryDao {
            return AppDatabase.getInstance(application).categoryDao()
        }

    }

}