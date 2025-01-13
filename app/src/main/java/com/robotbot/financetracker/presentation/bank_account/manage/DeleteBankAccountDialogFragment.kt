package com.robotbot.financetracker.presentation.bank_account.manage

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.robotbot.financetracker.R

class DeleteBankAccountDialogFragment : DialogFragment() {

    private var accountName: String = ACCOUNT_NAME_UNDEFINED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    private fun parseParams() {
        val args = requireArguments()
        accountName = args.getString(ACCOUNT_NAME_KEY)
            ?: throw IllegalArgumentException("Param account name is absent")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(getString(R.string.dialog_message_delete_account, accountName))
                .setPositiveButton(getString(R.string.dialog_delete_account_button_confirm)) { _, _ ->
                    parentFragmentManager.setFragmentResult(
                        REQUEST_KEY,
                        Bundle().apply { putBoolean(RESULT_KEY, true) }
                    )
                }
                .setNegativeButton(getString(R.string.dialog_delete_account_button_cancel)) { _, _ ->
                }
            builder.create()
        } ?: throw IllegalStateException("Activity == null")
    }

    interface DeleteAccountDialogListener {
        fun onDialogPositiveClick()
    }

    companion object {

        private const val ACCOUNT_NAME_KEY = "account_name"
        private const val ACCOUNT_NAME_UNDEFINED = ""
        const val REQUEST_KEY = "delete_account_request"
        const val RESULT_KEY = "delete_account_result"

        fun newInstance(accountName: String): DeleteBankAccountDialogFragment {
            return DeleteBankAccountDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ACCOUNT_NAME_KEY, accountName)
                }
            }
        }

    }
}