package com.robotbot.financetracker.di

import androidx.lifecycle.ViewModel
import com.robotbot.financetracker.presentation.BankAccountViewModel
import com.robotbot.financetracker.presentation.CreateBankAccountViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(BankAccountViewModel::class)
    fun bindBankAccountViewModel(viewModel: BankAccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateBankAccountViewModel::class)
    fun bindCreateBankAccountViewModel(viewModel: CreateBankAccountViewModel): ViewModel

}