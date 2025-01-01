package com.robotbot.financetracker.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.robotbot.financetracker.R

class DeleteCategoryDialogFragment : DialogFragment() {

    private lateinit var deleteCategoryDialogListener: DeleteCategoryDialogListener

    private var categoryName: String = CATEGORY_NAME_UNDEFINED

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DeleteCategoryDialogListener) {
            deleteCategoryDialogListener = context
        } else {
            throw ClassCastException("Activity must implement DeleteCategoryDialogListener")
        }
    }

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
            val builder = AlertDialog.Builder(it)
            builder.setMessage(getString(R.string.dialog_message_delete_category, categoryName))
                .setPositiveButton(getString(R.string.dialog_delete_account_button_confirm)) { _, _ ->
                    deleteCategoryDialogListener.onDialogPositiveClick()
                }
                .setNegativeButton(getString(R.string.dialog_delete_account_button_cancel)) { _, _ ->
                }
            builder.create()
        } ?: throw IllegalStateException("Activity == null")
    }

    interface DeleteCategoryDialogListener {
        fun onDialogPositiveClick()
    }

    companion object {

        private const val CATEGORY_NAME_KEY = "category_name"
        private const val CATEGORY_NAME_UNDEFINED = ""

        fun newInstance(categoryName: String): DeleteCategoryDialogFragment {
            return DeleteCategoryDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(CATEGORY_NAME_KEY, categoryName)
                }
            }
        }

    }
}