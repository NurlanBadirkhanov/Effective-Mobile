package com.effective.effectivemobile.helpers

import android.content.Context
import androidx.core.content.edit

class AuthPrefs(context: Context) {
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    fun isSignedIn(): Boolean = prefs.getBoolean(KEY_SIGNED_IN, false)

    fun setSignedIn(value: Boolean) {
        prefs.edit { putBoolean(KEY_SIGNED_IN, value) }
    }

    fun clear() {
        prefs.edit { clear() }
    }

    private companion object {
        const val KEY_SIGNED_IN = "signed_in"
    }
}
