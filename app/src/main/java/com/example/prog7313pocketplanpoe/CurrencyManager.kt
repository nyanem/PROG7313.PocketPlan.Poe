package com.example.prog7313pocketplanpoe



import android.content.Context
import android.content.SharedPreferences

object CurrencyManager {
    private const val PREF_NAME = "CurrencyPrefs"
    private const val KEY_CURRENCY = "selected_currency"
    private const val KEY_CURRENCY_CODE = "selected_currency_code"
    private const val KEY_CURRENCY_SYMBOL = "selected_currency_symbol"

    // Supported currencies with their code and symbol
    private val currencyData = mapOf(
        "Rands (ZAR)" to Pair("ZAR", "R"),
        "Dollars (USD)" to Pair("USD", "$"),
        "Euros (EUR)" to Pair("EUR", "€"),
        "Pounds (GBP)" to Pair("GBP", "£")
    )

    // Save the selected currency
    fun setCurrentCurrency(context: Context, currencyName: String) {
        val prefs = getPreferences(context)
        val currencyInfo = currencyData[currencyName] ?: Pair("ZAR", "R")

        prefs.edit().apply {
            putString(KEY_CURRENCY, currencyName)
            putString(KEY_CURRENCY_CODE, currencyInfo.first)
            putString(KEY_CURRENCY_SYMBOL, currencyInfo.second)
            apply()
        }
    }

    fun getCurrentCurrency(context: Context): String {
        return getPreferences(context).getString(KEY_CURRENCY, "Rands (ZAR)") ?: "Rands (ZAR)"
    }

    fun getCurrentCurrencyCode(context: Context): String {
        return getPreferences(context).getString(KEY_CURRENCY_CODE, "ZAR") ?: "ZAR"
    }

    fun getCurrentCurrencySymbol(context: Context): String {
        return getPreferences(context).getString(KEY_CURRENCY_SYMBOL, "R") ?: "R"
    }

    // Format a Double with current currency symbol (e.g., R700.00)
    fun formatCurrency(context: Context, amount: Double): String {
        val symbol = getCurrentCurrencySymbol(context)
        return "$symbol${String.format("%.2f", amount)}"
    }

    // Format a String with currency symbol (useful if already formatted)
//    fun formatCurrency(context: Context, amount: String): String {
//        val symbol = getCurrentCurrencySymbol(context)
//        return "$symbol$amount"
//    }

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
}
