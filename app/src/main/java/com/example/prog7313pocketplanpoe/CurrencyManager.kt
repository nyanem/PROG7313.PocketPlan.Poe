package com.example.prog7313pocketplanpoe


import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi

object CurrencyManager {
    private const val PREF_NAME = "CurrencyPrefs"
    private const val KEY_CURRENCY = "selected_currency"
    private const val KEY_CURRENCY_CODE = "selected_currency_code"
    private const val KEY_CURRENCY_SYMBOL = "selected_currency_symbol"

    // Currency data
    private val currencyData = mapOf(
        "Rands (ZAR)" to Pair("ZAR", "R"),
        "Dollars (USD)" to Pair("USD", "$"),
        "Euros (EUR)" to Pair("EUR", "€"),
        "Pounds (GBP)" to Pair("GBP", "£")
    )

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    fun setCurrentCurrency(context: Context, currencyName: String) {
        val prefs = getPreferences(context)
        val currencyInfo = currencyData[currencyName]

        prefs.edit().apply {
            putString(KEY_CURRENCY, currencyName)
            putString(KEY_CURRENCY_CODE, currencyInfo?.first ?: "ZAR")
            putString(KEY_CURRENCY_SYMBOL, currencyInfo?.second ?: "R")
            apply()
        }
    }

    fun getCurrentCurrency(context: Context): String {
        val prefs = getPreferences(context)
        return prefs.getString(KEY_CURRENCY, "Rands (ZAR)") ?: "Rands (ZAR)"
    }

    fun getCurrentCurrencyCode(context: Context): String {
        val prefs = getPreferences(context)
        return prefs.getString(KEY_CURRENCY_CODE, "ZAR") ?: "ZAR"
    }

    fun getCurrentCurrencySymbol(context: Context): String {
        val prefs = getPreferences(context)
        return prefs.getString(KEY_CURRENCY_SYMBOL, "R") ?: "R"
    }

    fun formatCurrency(context: Context, amount: Double): String {
        val symbol = getCurrentCurrencySymbol(context)
        return "$symbol${String.format("%.2f", amount)}"
    }

    fun formatCurrency(context: Context, amount: String): String {
        val symbol = getCurrentCurrencySymbol(context)
        return "$symbol$amount"
    }

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
}