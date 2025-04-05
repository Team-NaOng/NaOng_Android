package com.example.presentation.button.base

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
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

        val attributes = context.obtainStyledAttributes(
            attrs,
            R.styleable.BaseButton,
            defStyleAttr,
            defStyleRes
        )

        val buttonEnabled = attributes.getBoolean(R.styleable.BaseButton_android_enabled, true)
        val buttonText = attributes.getString(R.styleable.BaseButton_android_text)
        val textAppearanceRes = attributes.getResourceId(R.styleable.BaseButton_android_textAppearance, INVALID_RES_ID)
        val textSizePx = attributes.getDimension(R.styleable.BaseButton_android_textSize, INVALID_SIZE)
        val textColorStateList = attributes.getColorStateList(R.styleable.BaseButton_android_textColor)
        val ellipsizeMode = attributes.getInt(R.styleable.BaseButton_android_ellipsize, 3)
        val maxLineCount = attributes.getInt(R.styleable.BaseButton_android_maxLines, 2)

        attributes.recycle()

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
            text.also { this.text = it }
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

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeAllViews()
        buttonTextView = null
    }

    companion object {
        private const val INVALID_RES_ID = -1
        private const val INVALID_SIZE = -1f
    }
}
