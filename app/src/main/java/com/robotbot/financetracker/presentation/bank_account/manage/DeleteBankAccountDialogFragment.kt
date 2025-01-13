package com.robotbot.financetracker.presentation.bank_account.manage

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.robotbot.financetracker.R

class DeleteBankAccountDialogFragment : DialogFragment() {

    private val args by navArgs<DeleteBankAccountDialogFragmentArgs>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(getString(R.string.dialog_message_delete_account, args.accountName))
                .setPositiveButton(getString(R.string.dialog_delete_account_button_confirm)) { _, _ ->
                    setFragmentResult(
                        REQUEST_KEY,
                        Bundle().apply { putBoolean(RESULT_KEY, true) }
                    )
                    findNavController().popBackStack()
                }
                .setNegativeButton(getString(R.string.dialog_delete_account_button_cancel)) { _, _ ->
                }
            builder.create()
        } ?: throw IllegalStateException("Activity == null")
    }

    companion object {

        const val REQUEST_KEY = "delete_account_request"
        const val RESULT_KEY = "delete_account_result"

    }
}