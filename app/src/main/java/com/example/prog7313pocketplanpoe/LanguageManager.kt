package com.example.prog7313pocketplanpoe

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LanguageManager {
    private const val PREFS_NAME = "LanguagePrefs"
    private const val LANGUAGE_KEY = "language"
    private const val DEFAULT_LANGUAGE = "en"

    fun setLanguage(context: Context, languageCode: String) {
        try {
            android.util.Log.d("LanguageManager", "Setting language to: $languageCode")
            val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            sharedPrefs.edit().putString(LANGUAGE_KEY, languageCode).apply()

            // Apply the locale immediately
            setAppLocale(context, languageCode)
            android.util.Log.d("LanguageManager", "Language set successfully to: $languageCode")
        } catch (e: Exception) {
            android.util.Log.e("LanguageManager", "Error setting language to $languageCode", e)
            throw e
        }
    }
    fun getCurrentLanguage(context: Context): String {
        try {
            val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val language = sharedPrefs.getString(LANGUAGE_KEY, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
            android.util.Log.d("LanguageManager", "Current language: $language")
            return language
        } catch (e: Exception) {
            android.util.Log.e("LanguageManager", "Error getting current language, using default", e)
            return DEFAULT_LANGUAGE
        }
    }    fun setAppLocale(context: Context, languageCode: String) {
        try {
            android.util.Log.d("LanguageManager", "Setting app locale to: $languageCode")

            val locale = Locale(languageCode)
            Locale.setDefault(locale)

            android.util.Log.d("LanguageManager", "Creating configuration with locale: ${locale.displayName}")
            val config = Configuration(context.resources.configuration)
            config.setLocale(locale)

            // For newer Android versions, we rely on createConfigurationContext in attachBaseContext
            // For older versions, update the configuration directly
            try {
                @Suppress("DEPRECATION")
                context.resources.updateConfiguration(config, context.resources.displayMetrics)
                android.util.Log.d("LanguageManager", "Configuration updated successfully")
            } catch (e: Exception) {
                android.util.Log.w("LanguageManager", "Could not update configuration directly: ${e.message}")
            }

            android.util.Log.d("LanguageManager", "App locale set successfully to: ${locale.language} (${locale.displayName})")
        } catch (e: Exception) {
            android.util.Log.e("LanguageManager", "Error setting app locale to $languageCode", e)
            throw e
        }
    }
    fun getLocalizedContext(context: Context, s: String): Context {
        try {
            val languageCode = getCurrentLanguage(context)
            android.util.Log.d("LanguageManager", "Creating localized context for language: $languageCode")

            val locale = Locale(languageCode)
            val config = Configuration(context.resources.configuration)
            config.setLocale(locale)

            val localizedContext = context.createConfigurationContext(config)
            android.util.Log.d("LanguageManager", "Localized context created successfully")
            return localizedContext
        } catch (e: Exception) {
            android.util.Log.e("LanguageManager", "Error creating localized context", e)
            return context
        }
    }
    // Helper method to check if language is supported
    fun isLanguageSupported(languageCode: String): Boolean {
        return try {
            when (languageCode) {
                "en" -> true
                "af" -> true
                "xh" -> {
                    // Check if Xhosa is actually supported by the system
                    val isSystemSupported = java.util.Locale.getAvailableLocales().any { it.language == "xh" }
                    android.util.Log.d("LanguageManager", "Xhosa system support: $isSystemSupported")
                    isSystemSupported
                }
                else -> {
                    android.util.Log.w("LanguageManager", "Unknown language code: $languageCode")
                    false
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("LanguageManager", "Error checking language support for $languageCode", e)
            false
        }
    }

    // Helper method to get display name for language
    fun getLanguageDisplayName(context: Context, languageCode: String): String {
        return try {
            when (languageCode) {
                "en" -> context.getString(R.string.english)
                "af" -> context.getString(R.string.afrikaans)
                "xh" -> context.getString(R.string.xhosa)
                else -> languageCode
            }
        } catch (e: Exception) {
            android.util.Log.e("LanguageManager", "Error getting display name for $languageCode", e)
            languageCode
        }
    }
}