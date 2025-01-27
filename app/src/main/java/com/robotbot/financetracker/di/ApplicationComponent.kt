package com.robotbot.financetracker.di

import android.app.Application
import com.robotbot.financetracker.presentation.bank_account.BankAccountFragment
import com.robotbot.financetracker.presentation.bank_account.BankAccountListFragment
import com.robotbot.financetracker.presentation.bank_account.TransferListFragment
import com.robotbot.financetracker.presentation.category.CategoryFragment
import com.robotbot.financetracker.presentation.bank_account.manage.ManageBankAccountFragment
import com.robotbot.financetracker.presentation.bank_account.transfer.choose_account.ChooseAccountDialog
import com.robotbot.financetracker.presentation.bank_account.transfer.CreateTransferFragment
import com.robotbot.financetracker.presentation.category.manage.ManageCategoryFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(bankAccountFragment: BankAccountFragment)

    fun inject(categoryFragment: CategoryFragment)

    fun inject(fragment: ManageCategoryFragment)

    fun inject(fragment: ManageBankAccountFragment)

    fun inject(fragment: CreateTransferFragment)

    fun inject(fragment: ChooseAccountDialog)

    fun inject(fragment: BankAccountListFragment)

    fun inject(fragment: TransferListFragment)


    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}