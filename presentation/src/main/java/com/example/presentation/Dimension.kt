package com.example.presentation

import android.content.Context

fun Int.dpToPx(context: Context): Int {
    return (this * context.resources.displayMetrics.density).toInt()
}

fun Int.dpToPxF(context: Context): Float =
    this * context.resources.displayMetrics.density

