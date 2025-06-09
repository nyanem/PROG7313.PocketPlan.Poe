package com.example.prog7313pocketplanpoe



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


class LanguageFragment : Fragment() {

    private lateinit var languageSpinner: Spinner
    private lateinit var currencySpinner: Spinner
    private lateinit var languages: Array<String>
    private lateinit var languageCodes: Array<String>
    private lateinit var currencies: Array<String>

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

        // Set up back button
        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        // Load resources
        languages = resources.getStringArray(R.array.language_options)
        languageCodes = resources.getStringArray(R.array.language_codes)
        currencies = resources.getStringArray(R.array.currency_array)

        setupLanguageSpinner()
        setupCurrencySpinner()
    }

    private fun setupLanguageSpinner() {
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item_language, languages)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_language)
        languageSpinner.adapter = adapter

        val currentLanguage = LanguageManager.getCurrentLanguage(requireContext())
        val position = languageCodes.indexOf(currentLanguage)
        if (position != -1) languageSpinner.setSelection(position)

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
        val position = currencies.indexOf(currentCurrency)
        if (position != -1) currencySpinner.setSelection(position)
        else currencySpinner.setSelection(0)

        currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                if (pos > 0) {
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
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
    private fun restartApp() {
        val context = requireContext()
        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        if (launchIntent != null) {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(launchIntent)
            requireActivity().finish()
        } else {
            android.util.Log.e("RestartApp", "Launch intent is null")
        }

    }
}
