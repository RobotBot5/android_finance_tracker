package com.robotbot.financetracker.di

import com.robotbot.financetracker.data.BankAccountMockRepository
import com.robotbot.financetracker.data.CategoryMockRepository
import com.robotbot.financetracker.domain.repotisories.BankAccountRepository
import com.robotbot.financetracker.domain.repotisories.CategoryRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    companion object {

        @Provides
        fun provideBankAccountMockRepository(): BankAccountRepository = BankAccountMockRepository

        @Provides
        fun provideCategoryMockRepository(): CategoryRepository = CategoryMockRepository

    }

}