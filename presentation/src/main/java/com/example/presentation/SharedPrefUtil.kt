package com.example.presentation

import android.content.Context
import android.content.SharedPreferences

class SharedPrefUtil private constructor() {

    companion object {
        private const val PREF_NAME = "onboarding_prefs"
        private const val KEY_FIRST_LAUNCH = "is_first_launch"

        private fun getPrefs(context: Context): SharedPreferences {
            return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }

        fun isFirstLaunch(context: Context): Boolean {
            return getBoolean(context, KEY_FIRST_LAUNCH, true)
        }

        fun setFirstLaunchCompleted(context: Context) {
            putBoolean(context, KEY_FIRST_LAUNCH, false)
        }

        fun getBoolean(context: Context, key: String, default: Boolean): Boolean {
            return getPrefs(context).getBoolean(key, default)
        }

        fun putBoolean(context: Context, key: String, value: Boolean) {
            getPrefs(context).edit().putBoolean(key, value).apply()
        }
    }
}
