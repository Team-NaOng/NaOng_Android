package com.example.presentation.chip

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.example.presentation.R

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
            17.dpToPx(), 8.dpToPx(),
            17.dpToPx(), 8.dpToPx()
        )
        includeFontPadding = false
        gravity = Gravity.CENTER

        setOnClickListener {
            isChipSelected = !isChipSelected
        }

        updateBackground()
    }

    private fun updateBackground() {
        background = if (isChipSelected) {
            ContextCompat.getDrawable(context, R.drawable.chip_selected)
        } else {
            ContextCompat.getDrawable(context, R.drawable.chip_unselected)
        }
    }

    private fun Int.dpToPx(): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}
