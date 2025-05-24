package com.example.presentation.view.button.base

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.example.presentation.R

abstract class BaseButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0,
    defStyleRes: Int = R.style.BaseButton,
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var buttonTextView: TextView? = null

    init {
        orientation = HORIZONTAL

        with(
            context.obtainStyledAttributes(
                attrs,
                R.styleable.BaseButton,
                defStyleAttr,
                defStyleRes
            )
        ) {
            val buttonEnabled = getBoolean(R.styleable.BaseButton_android_enabled, true)
            val buttonText = getString(R.styleable.BaseButton_android_text)
            val textAppearanceRes =
                getResourceId(R.styleable.BaseButton_android_textAppearance, INVALID_RES_ID)
            val textSizePx = getDimension(R.styleable.BaseButton_android_textSize, INVALID_SIZE)
            val textColorStateList = getColorStateList(R.styleable.BaseButton_android_textColor)
            val ellipsizeMode = getInt(R.styleable.BaseButton_android_ellipsize, 3)
            val maxLineCount = getInt(R.styleable.BaseButton_android_maxLines, 2)

            recycle()

            isEnabled = buttonEnabled

            setupTextView(
                context = context,
                text = buttonText,
                textAppearance = if (textAppearanceRes == INVALID_RES_ID) null else textAppearanceRes,
                textSize = if (textSizePx == INVALID_SIZE) null else textSizePx,
                textColor = textColorStateList,
                ellipsize = ellipsizeMode,
                maxLines = maxLineCount
            )
        }
    }

    private fun setupTextView(
        context: Context,
        text: String?,
        textAppearance: Int?,
        textSize: Float?,
        textColor: ColorStateList?,
        ellipsize: Int?,
        maxLines: Int?,
        underline: Boolean = false,
    ) {
        if (text.isNullOrEmpty()) return

        buttonTextView = TextView(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
            gravity = Gravity.CENTER
            textAppearance?.let { setTextAppearance(it) }
            textSize?.let { setTextSize(TypedValue.COMPLEX_UNIT_PX, it) }
            textColor?.let { setTextColor(it) }
            ellipsize?.let {
                this.ellipsize = when (it) {
                    0 -> TextUtils.TruncateAt.START
                    1 -> TextUtils.TruncateAt.MIDDLE
                    2 -> TextUtils.TruncateAt.END
                    else -> TextUtils.TruncateAt.MARQUEE
                }
            }
            maxLines?.let { this.maxLines = it }
            this.text = text
            if (underline) paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
        }
        addView(buttonTextView)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        buttonTextView?.isEnabled = enabled
    }

    override fun setPressed(pressed: Boolean) {
        super.setPressed(pressed)
        buttonTextView?.isPressed = pressed
    }

    fun setText(text: String) {
        if (buttonTextView == null) {
            buttonTextView = TextView(context)
            addView(buttonTextView)
        }
        buttonTextView?.text = text
    }


    companion object {
        private const val INVALID_RES_ID = -1
        private const val INVALID_SIZE = -1f
    }
}
