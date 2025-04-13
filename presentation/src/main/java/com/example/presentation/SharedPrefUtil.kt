package com.example.presentation

import android.content.Context
import android.content.SharedPreferences

class SharedPrefUtil private constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun isFirstLaunch(): Boolean {
        return prefs.getBoolean(KEY_FIRST_LAUNCH, true)
    }

    fun setFirstLaunchCompleted() {
        prefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply()
    }

    companion object {
        private const val PREF_NAME = "onboarding_prefs"
        private const val KEY_FIRST_LAUNCH = "is_first_launch"

        @Volatile
        private var INSTANCE: SharedPrefUtil? = null

        fun getInstance(context: Context): SharedPrefUtil {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SharedPrefUtil(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }
}
