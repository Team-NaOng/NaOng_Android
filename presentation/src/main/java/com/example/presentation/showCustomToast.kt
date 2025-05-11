package com.example.presentation

import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast

fun showCustomToast(view: View, message: String) {
    val layoutInflater = LayoutInflater.from(view.context)
    val layout = layoutInflater.inflate(R.layout.toast_custom, null)

    val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    layout.minimumWidth = (screenWidth * 0.140).toInt() // 기기 비율 기반 최소 너비 적용

    val textView = layout.findViewById<TextView>(R.id.textMessage)
    textView.text = message

    val toast = Toast(view.context)
    toast.duration = Toast.LENGTH_SHORT
    toast.view = layout
    toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 220)
    toast.show()
}
