package com.example.presentation.view.chip

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.example.presentation.R
import com.example.presentation.dpToPx

class CustomChipView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : AppCompatTextView(context, attrs) {

    var isChipSelected = false
        set(value) {
            field = value
            updateBackground()
        }

    init {
        setTextAppearance(R.style.description)

        setPadding(
            17.dpToPx(context), 8.dpToPx(context),
            17.dpToPx(context), 8.dpToPx(context)
        )
        includeFontPadding = false
        gravity = Gravity.CENTER

        updateBackground()
    }

    private fun updateBackground() {
        background = if (isChipSelected) {
            ContextCompat.getDrawable(context, R.drawable.chip_selected)
        } else {
            ContextCompat.getDrawable(context, R.drawable.chip_unselected)
        }
    }
}
