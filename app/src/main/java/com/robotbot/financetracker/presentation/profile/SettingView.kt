package com.robotbot.financetracker.presentation.profile

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.robotbot.financetracker.R
import com.robotbot.financetracker.databinding.ItemSettingBinding

class SettingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: ItemSettingBinding

    var settingName: String? = UNDEFINED_STRING
        set(value) {
            field = value
            binding.tvName.text = value ?: UNDEFINED_STRING
        }

    var settingValue: String? = UNDEFINED_STRING
        set(value) {
            field = value
            binding.tvValue.text = value ?: UNDEFINED_STRING
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.item_setting, this, true)
        binding = ItemSettingBinding.bind(this)
        initAttributes(attrs, defStyleAttr, defStyleRes)
    }

    private fun initAttributes(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingView, defStyleAttr, defStyleRes)

        settingName = typedArray.getString(R.styleable.SettingView_setting_name)
        settingValue = typedArray.getString(R.styleable.SettingView_setting_value)

        typedArray.recycle()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()!!
        val savedState = SavedState(superState)
        savedState.settingName = settingName
        savedState.settingValue = settingValue
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        settingName = savedState.settingName
        settingValue = savedState.settingValue
    }

    class SavedState : BaseSavedState {

        var settingName: String? = null
        var settingValue: String? = null

        constructor(superState: Parcelable) : super(superState)
        constructor(parcel: Parcel) : super(parcel) {
            settingName = parcel.readString()
            settingValue = parcel.readString()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(settingName)
            out.writeString(settingValue)
        }

        companion object {

            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel): SavedState {
                    return SavedState(source)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return Array(size) { null }
                }
            }
        }
    }

    companion object {
        private const val UNDEFINED_STRING = "Undefined"
    }
}