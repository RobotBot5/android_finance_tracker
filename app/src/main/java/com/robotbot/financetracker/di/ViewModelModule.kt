package com.robotbot.financetracker.di

import androidx.lifecycle.ViewModel
import com.robotbot.financetracker.presentation.bank_account.BankAccountViewModel
import com.robotbot.financetracker.presentation.bank_account.TransferHistoryViewModel
import com.robotbot.financetracker.presentation.category.CategoryViewModel
import com.robotbot.financetracker.presentation.category.manage.ManageCategoryViewModel
import com.robotbot.financetracker.presentation.bank_account.manage.ManageBankAccountViewModel
import com.robotbot.financetracker.presentation.bank_account.transfer.choose_account.ChooseAccountViewModel
import com.robotbot.financetracker.presentation.bank_account.transfer.CreateTransferViewModel
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
    @ViewModelKey(ManageBankAccountViewModel::class)
    fun bindCreateBankAccountViewModel(viewModel: ManageBankAccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel::class)
    fun bindCategoryViewModel(viewModel: CategoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ManageCategoryViewModel::class)
    fun bindCreateCategoryViewModel(viewModel: ManageCategoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateTransferViewModel::class)
    fun bindCreateTransferViewModel(viewModel: CreateTransferViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChooseAccountViewModel::class)
    fun bindChooseAccountViewModel(viewModel: ChooseAccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TransferHistoryViewModel::class)
    fun bindTransferHistoryViewModel(viewModel: TransferHistoryViewModel): ViewModel

}