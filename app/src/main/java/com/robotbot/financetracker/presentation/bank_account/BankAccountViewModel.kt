package com.robotbot.financetracker.presentation.bank_account

import androidx.lifecycle.ViewModel
import com.robotbot.financetracker.domain.usecases.account.GetBankAccountListUseCase
import javax.inject.Inject

class BankAccountViewModel @Inject constructor(
    getBankAccountListUseCase: GetBankAccountListUseCase
) : ViewModel() {

    val bankAccounts = getBankAccountListUseCase()

}