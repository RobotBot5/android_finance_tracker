package com.robotbot.financetracker

import android.app.Application
import com.robotbot.financetracker.di.DaggerApplicationComponent

class FinanceTrackerApp : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

}