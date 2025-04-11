package com.example.presentation.button

import android.content.Context
import android.util.AttributeSet
import com.example.presentation.R
import com.example.presentation.button.base.BaseButton

class ButtonSquare @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0,
    defStyleRes: Int = R.style.ButtonSquareStyle,
) : BaseButton(context, attrs, defStyleAttr, defStyleRes)