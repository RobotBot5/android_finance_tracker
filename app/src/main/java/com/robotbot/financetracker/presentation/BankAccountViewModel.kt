package com.robotbot.financetracker.presentation

import androidx.lifecycle.ViewModel
import com.robotbot.financetracker.domain.usecases.account.GetBankAccountListUseCase
import javax.inject.Inject

class BankAccountViewModel @Inject constructor(
    getBankAccountListUseCase: GetBankAccountListUseCase
) : ViewModel() {

    val bankAccounts = getBankAccountListUseCase()

}