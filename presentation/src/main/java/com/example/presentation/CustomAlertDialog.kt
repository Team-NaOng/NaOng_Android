package com.example.presentation

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.presentation.databinding.DialogCustomAlertBinding

class CustomAlertDialog private constructor(
    private val dialog: AlertDialog,
    private val binding: DialogCustomAlertBinding
) {
    class Builder(private val context: Context) {
        private var title: String? = null
        private var content: String? = null
        private var positiveText: String? = null
        private var negativeText: String? = null
        private var onPositiveClick: (() -> Unit)? = null
        private var onNegativeClick: (() -> Unit)? = null
        private var isCancelable: Boolean = true
        fun setCancelable(cancelable: Boolean) = apply { this.isCancelable = cancelable }

        fun setTitle(title: String) = apply { this.title = title }
        fun setContent(content: String) = apply { this.content = content }
        fun setPositiveButton(text: String, onClick: (() -> Unit)? = null) = apply {
            this.positiveText = text
            this.onPositiveClick = onClick
        }

        fun setNegativeButton(text: String, onClick: (() -> Unit)? = null) = apply {
            this.negativeText = text
            this.onNegativeClick = onClick
        }

        fun show(): CustomAlertDialog {
            val inflater = LayoutInflater.from(context)
            val binding = DialogCustomAlertBinding.inflate(inflater)
            val view = binding.root

            val dialog = AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(isCancelable)
                .create()

            with(binding) {
                title?.let {
                    textViewTitle.text = it
                    textViewTitle.visibility = View.VISIBLE
                }

                content?.let {
                    textViewContent.text = it
                    textViewContent.visibility = View.VISIBLE
                }

                btnPositive.apply {
                    setText(positiveText ?: "확인")
                    setOnClickListener {
                        onPositiveClick?.invoke()
                        dialog.dismiss()
                    }
                }

                btnNegative.apply {
                    if (negativeText != null) {
                        setText(negativeText ?: "취소")
                        setOnClickListener {
                            onNegativeClick?.invoke()
                            dialog.dismiss()
                        }
                    } else {
                        visibility = View.GONE
                    }
                }
            }

            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val marginHorizontalPx = 24.dpToPx(context)
            dialog.window?.setLayout(
                context.resources.displayMetrics.widthPixels - (marginHorizontalPx * 2),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            return CustomAlertDialog(dialog, binding)
        }
    }
}
