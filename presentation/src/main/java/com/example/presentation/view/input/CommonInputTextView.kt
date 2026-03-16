package com.example.presentation.view.input

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.presentation.R

class CommonInputTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    private var inputState: InputState = InputState.NORMAL

    init {
        isFocusable = true
        isFocusableInTouchMode = true
        isClickable = true
        isCursorVisible = true

        background = ContextCompat.getDrawable(context, R.drawable.bg_input_text)
        setTextColor(ContextCompat.getColor(context, R.color.black))
        setHintTextColor(ContextCompat.getColor(context, R.color.gray2))
        setPadding(32, 24, 32, 24)
    }

    fun setState(state: InputState) {
        inputState = state
        when (state) {
            InputState.DISABLE -> {
                isEnabled = false
                isFocusable = false
                isFocusableInTouchMode = false
            }

            InputState.NORMAL -> {
                isEnabled = true
                isFocusable = true
                isFocusableInTouchMode = true
                clearFocus()
            }

            InputState.FOCUS -> {
                isEnabled = true
                isFocusable = true
                isFocusableInTouchMode = true
                requestFocus()
            }
        }
    }
}