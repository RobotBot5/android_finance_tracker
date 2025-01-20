package com.robotbot.financetracker.presentation.bank_account.transfer

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.util.Calendar

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private val args by navArgs<DatePickerFragmentArgs>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val prevDate = args.prevDate
        val year = prevDate.get(Calendar.YEAR)
        val month = prevDate.get(Calendar.MONTH)
        val day = prevDate.get(Calendar.DAY_OF_MONTH)

        return object : DatePickerDialog(requireContext(), this, year, month, day) {
            override fun onDateChanged(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
                setDateInBundle(year, month, dayOfMonth)
                dismiss()
            }
        }

//        return DatePickerDialog(requireContext(), this, year, month, day)
    }



    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        setDateInBundle(year, month, dayOfMonth)
    }

    private fun setDateInBundle(year: Int, month: Int, dayOfMonth: Int) {
        setFragmentResult(
            REQUEST_KEY,
            Bundle().apply {
                putSerializable(RESULT_KEY,
                    Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }
                )
            }
        )
    }



    companion object {

        const val REQUEST_KEY = "selected_date_request"
        const val RESULT_KEY = "selected_date__result"

    }
}