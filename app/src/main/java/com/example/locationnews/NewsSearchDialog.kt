package com.example.locationnews

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.*


interface SearchInterface {
    fun searchHeadlines(params: HashMap<String, String>)
    fun search(params: HashMap<String, String>)
}

class NewsSearchDialog(context: Context) : Dialog(context) {

    private var searchListeners: SearchInterface? = if(context is SearchInterface) context as SearchInterface else null

    lateinit var spinner1: Spinner
    lateinit var spinner2: Spinner

    lateinit var spinner1Title: TextView
    lateinit var spinner2Title: TextView

    var isHeadline = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_search_dialog)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        this.window?.setLayout(
            800,
            800
        )

        spinner1 = findViewById(R.id.spinner)
        spinner2 = findViewById(R.id.spinner2)

        spinner1Title = findViewById(R.id.spinner1Name)
        spinner2Title = findViewById(R.id.spinner2Name)

        val headLineSwitch = findViewById<Switch>(R.id.headlineSwitch)
        headLineSwitch.setOnCheckedChangeListener { _, _ ->
            isHeadline = !isHeadline
            switchContentsSpinners(isHeadline)
        }

        val searchButton = findViewById<Button>(R.id.newsSearchButton)
        searchButton.setOnClickListener {
            var params = HashMap<String, String>()
            params["q"] = findViewById<EditText>(R.id.keywordEntry).text.toString()
            if(isHeadline){
                params["country"] = SearchConstants.countryCodes[
                        spinner1.selectedItem.toString()
                ].toString()

                params["category"] = SearchConstants.languageCodes[
                        spinner2.selectedItem.toString()
                ].toString()
                searchListeners?.searchHeadlines(params)
            }
            else {
                params["language"] = SearchConstants.languageCodes[
                        spinner1.selectedItem.toString()
                ].toString()

                params["sortBy"] = SearchConstants.languageCodes[
                        spinner2.selectedItem.toString()
                ].toString()
                searchListeners?.search(params)
            }
            Log.d("values = ", params.toString())
            dismiss()
        }
        switchContentsSpinners(isHeadline)
    }



    private fun switchContentsSpinners(isHeadlines: Boolean) {
        if(isHeadlines) {
            setSpinners(
                SearchConstants.countryCodes.keys.toTypedArray(),
                "Country",
                SearchConstants.categoryOptions.keys.toTypedArray(),
                "Category"
            )
        }
        else {
            setSpinners(
                SearchConstants.languageCodes.keys.toTypedArray(),
                "Language",
                SearchConstants.sortByOptions.keys.toTypedArray(),
                "Sort by"
            )
        }
    }

    private fun setSpinners(list1: Array<String>, name1: String, list2: Array<String>, name2: String) {
        val spinnerAdapter = ArrayAdapter<String>(context, R.layout.news_spinner_item, list1)
        spinnerAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        spinner1.adapter = spinnerAdapter
        spinner1Title.text = name1

        val spinnerAdapter2 = ArrayAdapter<String>(context, R.layout.news_spinner_item, list2)
        spinnerAdapter2.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        spinner2.adapter = spinnerAdapter2
        spinner2Title.text = name2
    }

}

