package com.example.prog7313pocketplanpoe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class LanguageFragment : Fragment() {

    private lateinit var languageSpinner: Spinner
    private lateinit var currencySpinner: Spinner
    private lateinit var languages: Array<String>
    private lateinit var languageCodes: Array<String>
    private lateinit var currencies: Array<String>

    interface LanguageChangeListener {
        fun onLanguageChanged()
        fun onFragmentBackPressed()
    }

    private var languageChangeListener: LanguageChangeListener? = null

    fun setLanguageChangeListener(listener: LanguageChangeListener) {
        this.languageChangeListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        languageSpinner = view.findViewById(R.id.languageSpinner)
        currencySpinner = view.findViewById(R.id.currencySpinner)
        val backButton = view.findViewById<ImageButton>(R.id.backButton)

        languages = resources.getStringArray(R.array.language_options)
        languageCodes = resources.getStringArray(R.array.language_codes)
        currencies = resources.getStringArray(R.array.currency_array)

        setupLanguageSpinner()
        setupCurrencySpinner()

        backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupLanguageSpinner() {
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item_language, languages)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_language)
        languageSpinner.adapter = adapter

        val currentLanguage = LanguageManager.getCurrentLanguage(requireContext())
        val currentIndex = languageCodes.indexOf(currentLanguage)
        if (currentIndex != -1) languageSpinner.setSelection(currentIndex)

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val selectedCode = languageCodes[pos]
                val selectedName = languages[pos]
                val savedLang = LanguageManager.getCurrentLanguage(requireContext())

                if (selectedCode != savedLang) {
                    LanguageManager.setLanguage(requireContext(), selectedCode)
                    Toast.makeText(
                        requireContext(),
                        "${getString(R.string.language_changed)} $selectedName",
                        Toast.LENGTH_SHORT
                    ).show()
                    restartApp()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupCurrencySpinner() {
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item_language, currencies)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_language)
        currencySpinner.adapter = adapter

        val currentCurrency = CurrencyManager.getCurrentCurrency(requireContext())
        val currentIndex = currencies.indexOf(currentCurrency)
        currencySpinner.setSelection(if (currentIndex != -1) currentIndex else 0)

        currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val selectedCurrency = currencies[pos]
                val savedCurrency = CurrencyManager.getCurrentCurrency(requireContext())

                if (selectedCurrency != savedCurrency) {
                    CurrencyManager.setCurrentCurrency(requireContext(), selectedCurrency)
                    Toast.makeText(
                        requireContext(),
                        "${getString(R.string.currency_changed)} $selectedCurrency",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun restartApp() {
        try {
            val context = requireContext()
            val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(launchIntent)
                requireActivity().finish()
            } else {
                val intent = Intent(context, LandingPageFragment::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                requireActivity().finish()
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error restarting app: ${e.message}", Toast.LENGTH_SHORT).show()
            languageChangeListener?.onLanguageChanged()
        }
    }
}