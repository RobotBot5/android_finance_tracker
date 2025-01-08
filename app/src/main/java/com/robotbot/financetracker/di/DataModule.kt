package com.robotbot.financetracker.di

import android.app.Application
import com.robotbot.financetracker.data.repository.BankAccountMockRepository
import com.robotbot.financetracker.data.repository.CategoryMockRepository
import com.robotbot.financetracker.data.repository.CategoryRepositoryImpl
import com.robotbot.financetracker.data.database.AppDatabase
import com.robotbot.financetracker.data.database.dao.BankAccountDao
import com.robotbot.financetracker.data.database.dao.CategoryDao
import com.robotbot.financetracker.data.network.ApiFactory
import com.robotbot.financetracker.data.network.ApiService
import com.robotbot.financetracker.data.repository.BankAccountRepositoryImpl
import com.robotbot.financetracker.domain.repotisories.BankAccountRepository
import com.robotbot.financetracker.domain.repotisories.CategoryRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    @RealCategoryDatabaseQualifier
    fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    @RealBankAccountDatabaseQualifier
    fun bindBankAccountRepository(impl: BankAccountRepositoryImpl): BankAccountRepository

    companion object {

        @Provides
        @MockBankAccountDatabaseQualifier
        fun provideBankAccountMockRepository(): BankAccountRepository = BankAccountMockRepository

        @Provides
        @MockCategoryDatabaseQualifier
        fun provideCategoryMockRepository(): CategoryRepository = CategoryMockRepository

        @ApplicationScope
        @Provides
        fun provideCategoryDao(application: Application): CategoryDao {
            return AppDatabase.getInstance(application).categoryDao()
        }

        @ApplicationScope
        @Provides
        fun provideBankAccountDao(application: Application): BankAccountDao {
            return AppDatabase.getInstance(application).bankAccountDao()
        }

        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }

    }

}