package com.example.locationnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.locationnews.model.*
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Type

lateinit var viewModel: NewsViewModel

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpJsonSearchConstants()

        val repository = NewsRepository()
        val viewModelFactory = NewsViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewsViewModel::class.java)

        val params = hashMapOf<String, String>(
            "country" to "ca",
            "category" to "business"
        )


        val newsList = mutableListOf<NewsGet>()
        val newsRecycler = findViewById<RecyclerView>(R.id.NewsListView)
        newsRecycler.layoutManager = LinearLayoutManager(this)
        newsRecycler.adapter = NewsViewRecyclerView(newsList, this, layoutInflater)

        viewModel.getHeadlinesPost(params)
        viewModel.modelResponse.observe(this, Observer { response ->
            if(response.isSuccessful){
                val responseItems = response.body()
                if (responseItems != null) {
                    for(value in responseItems) {
                        Log.d("------", "")
                        Log.d("Publisher", value.publisher)
                        Log.d("Title", value.title)
                        Log.d("Description", value.description)
                        Log.d("URL", value.urlToPage)
                        Log.d("Published At", value.publishedAt)
                    }
                    (newsRecycler.adapter as NewsViewRecyclerView).updateList(responseItems)
                }
            }
        })

        NewsSearchDialog(this).show()
    }

    private fun setUpJsonSearchConstants() {
        if (SearchConstants.countryCodes.isEmpty()) {
            SearchConstants.countryCodes = getJsonFromFile(
                "country_codes.json",
                SearchConstants.getCountryCodeGson(),
                SearchConstants.codesType)!!
            if(SearchConstants.countryCodes != null) {
                for (constant in SearchConstants.countryCodes){
                    Log.d("s", constant.toString())
                }
            }
        }
        if (SearchConstants.languageCodes.isEmpty()) {
            SearchConstants.languageCodes = getJsonFromFile(
                "language_codes.json",
                SearchConstants.getLanguageCodeGson(),
                SearchConstants.codesType)!!
            if(SearchConstants.languageCodes != null) {
                for (constant in SearchConstants.languageCodes){
                    Log.d("d", constant.toString())
                }
            }
        }
    }


    private fun getJsonFromFile(filename: String, gson: Gson, type: Type) : HashMap<String, String>? {
        return try{
            val inputStream: InputStream = assets.open(filename)
            val string = inputStream.bufferedReader().use { it.readText() }
            gson.fromJson(string, type)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            null
        }
    }
}
