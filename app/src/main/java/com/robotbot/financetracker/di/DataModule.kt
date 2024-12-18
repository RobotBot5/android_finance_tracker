package com.robotbot.financetracker.di

import com.robotbot.financetracker.data.BankAccountMockRepository
import com.robotbot.financetracker.domain.repotisories.BankAccountRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    companion object {

        @Provides
        fun provideBankAccountMockRepository(): BankAccountRepository = BankAccountMockRepository

    }

}