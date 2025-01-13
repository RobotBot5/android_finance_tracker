package com.robotbot.financetracker.presentation.category.manage

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.robotbot.financetracker.R

class DeleteCategoryDialogFragment : DialogFragment() {

    private val args by navArgs<DeleteCategoryDialogFragmentArgs>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it)
                .setMessage(getString(R.string.dialog_message_delete_category, args.categoryName))
                .setPositiveButton(getString(R.string.dialog_delete_account_button_confirm)) { _, _ ->
                    parentFragmentManager.setFragmentResult(
                        REQUEST_KEY,
                        Bundle().apply { putBoolean(RESULT_KEY, true) }
                    )
                    findNavController().popBackStack()
                }
                .setNegativeButton(getString(R.string.dialog_delete_account_button_cancel)) { _, _ ->
                }
                .create()
        } ?: throw IllegalStateException("Activity == null")
    }

    companion object {
        const val REQUEST_KEY = "delete_category_request"
        const val RESULT_KEY = "delete_category_result"
    }
}