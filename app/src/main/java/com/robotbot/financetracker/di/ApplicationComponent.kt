package com.robotbot.financetracker.di

import android.app.Application
import com.robotbot.financetracker.presentation.bank_account.BankAccountFragment
import com.robotbot.financetracker.presentation.category.CategoryFragment
import com.robotbot.financetracker.presentation.category.manage.ManageCategoryActivity
import com.robotbot.financetracker.presentation.bank_account.manage.ManageBankAccountActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(bankAccountFragment: BankAccountFragment)

    fun inject(manageBankAccountActivity: ManageBankAccountActivity)

    fun inject(categoryFragment: CategoryFragment)

    fun inject(manageCategoryActivity: ManageCategoryActivity)


    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}