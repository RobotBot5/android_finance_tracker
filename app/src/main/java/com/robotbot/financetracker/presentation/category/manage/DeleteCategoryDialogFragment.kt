package com.robotbot.financetracker.presentation.category.manage

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.robotbot.financetracker.R

class DeleteCategoryDialogFragment : DialogFragment() {

    private var categoryName: String = CATEGORY_NAME_UNDEFINED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    private fun parseParams() {
        val args = requireArguments()
        categoryName = args.getString(CATEGORY_NAME_KEY)
            ?: throw IllegalArgumentException("Param category name is absent")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it)
                .setMessage(getString(R.string.dialog_message_delete_category, categoryName))
                .setPositiveButton(getString(R.string.dialog_delete_account_button_confirm)) { _, _ ->
                    parentFragmentManager.setFragmentResult(
                        REQUEST_KEY,
                        Bundle().apply { putBoolean(RESULT_KEY, true) }
                    )
                }
                .setNegativeButton(getString(R.string.dialog_delete_account_button_cancel)) { _, _ ->
                }
                .create()
        } ?: throw IllegalStateException("Activity == null")
    }

    companion object {

        private const val CATEGORY_NAME_KEY = "category_name"
        private const val CATEGORY_NAME_UNDEFINED = ""
        const val REQUEST_KEY = "delete_category_request"
        const val RESULT_KEY = "delete_category_result"

        fun newInstance(categoryName: String): DeleteCategoryDialogFragment {
            return DeleteCategoryDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(CATEGORY_NAME_KEY, categoryName)
                }
            }
        }

    }
}