package com.robotbot.financetracker.di

import android.app.Application
import com.robotbot.financetracker.presentation.BankAccountFragment
import com.robotbot.financetracker.presentation.CategoryFragment
import com.robotbot.financetracker.presentation.CreateCategoryActivity
import com.robotbot.financetracker.presentation.ManageBankAccountActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(bankAccountFragment: BankAccountFragment)

    fun inject(manageBankAccountActivity: ManageBankAccountActivity)

    fun inject(categoryFragment: CategoryFragment)

    fun inject(createCategoryActivity: CreateCategoryActivity)


    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}