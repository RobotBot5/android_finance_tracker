package com.robotbot.financetracker.presentation.bank_account.manage

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.robotbot.financetracker.R

class DeleteBankAccountDialogFragment : DialogFragment() {

    private lateinit var deleteAccountDialogListener: DeleteAccountDialogListener

    private var accountName: String = ACCOUNT_NAME_UNDEFINED

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DeleteAccountDialogListener) {
            deleteAccountDialogListener = context
        } else {
            throw ClassCastException("Activity must implement DeleteAccountDialogListener")
        }
    }

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
                    deleteAccountDialogListener.onDialogPositiveClick()
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

        fun newInstance(accountName: String): DeleteBankAccountDialogFragment {
            return DeleteBankAccountDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ACCOUNT_NAME_KEY, accountName)
                }
            }
        }

    }
}