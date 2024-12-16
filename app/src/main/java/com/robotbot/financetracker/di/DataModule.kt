package com.robotbot.financetracker.di

import com.robotbot.financetracker.data.BankAccountMockRepository
import com.robotbot.financetracker.domain.repotisories.BankAccountRepository
import dagger.Binds
import dagger.Module

@Module
interface DataModule {

    @Binds
    fun bindBankAccountRepository(impl: BankAccountMockRepository): BankAccountRepository

}