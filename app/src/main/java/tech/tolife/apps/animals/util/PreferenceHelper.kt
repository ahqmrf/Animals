package tech.tolife.apps.animals.util

import android.content.Context

class PreferenceHelper(context: Context) {

    private val PREF_API_KEY = "apiKey"

    private val prefs by lazy {
        context.getSharedPreferences("pref", Context.MODE_PRIVATE)
    }

    fun saveApiKey(key: String?) {
        prefs.edit().putString(PREF_API_KEY, key).apply()
    }

    fun getApiKey() = prefs.getString(PREF_API_KEY, null)
}