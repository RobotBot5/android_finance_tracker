package com.robotbot.financetracker.di

import android.app.Application
import com.robotbot.financetracker.presentation.bank_account.BankAccountFragment
import com.robotbot.financetracker.presentation.category.CategoryFragment
import com.robotbot.financetracker.presentation.bank_account.manage.ManageBankAccountFragment
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


    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}