package com.example.presentation.util


import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun getTodayDateText(
    pattern: String = "yyyy년 M월 d일",
    locale: Locale = Locale.KOREAN
): String {
    return LocalDate.now().format(DateTimeFormatter.ofPattern(pattern, locale))
}