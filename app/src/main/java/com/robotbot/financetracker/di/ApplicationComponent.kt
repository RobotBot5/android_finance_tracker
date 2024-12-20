package com.robotbot.financetracker.di

import android.app.Application
import com.robotbot.financetracker.presentation.BankAccountFragment
import com.robotbot.financetracker.presentation.CreateBankAccountActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(bankAccountFragment: BankAccountFragment)

    fun inject(createBankAccountActivity: CreateBankAccountActivity)


    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}