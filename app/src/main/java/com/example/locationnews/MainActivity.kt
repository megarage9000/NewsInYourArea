package com.example.locationnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
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
lateinit var newsRecycler: RecyclerView


class MainActivity : AppCompatActivity(), SearchInterface{

    private lateinit var loading: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loading = LoadingDialog(this)
        setUpJsonSearchConstants()
        val repository = NewsRepository()
        val viewModelFactory = NewsViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewsViewModel::class.java)

        val newsList = mutableListOf<NewsGet>()
        newsRecycler = findViewById<RecyclerView>(R.id.NewsListView)
        newsRecycler.layoutManager = LinearLayoutManager(this)
        newsRecycler.adapter = NewsViewRecyclerView(newsList, this, layoutInflater)

        val searchButton = findViewById<Button>(R.id.startNewsSearch)
        searchButton.setOnClickListener {
            NewsSearchDialog(this).show()
        }

        var params = hashMapOf<String, String>(
            "q" to "bitcoin",
            "language" to "en",
            "sortBy" to "popularity"
        )

        search(params)
        trackGetRequest()
    }

    private fun setUpJsonSearchConstants() {
        if (SearchConstants.countryCodes.isEmpty()) {
            SearchConstants.countryCodes = getJsonFromFile(
                "country_codes.json",
                SearchConstants.getCountryCodeGson(),
                SearchConstants.codesType)!!.toSortedMap()
        }
        if (SearchConstants.languageCodes.isEmpty()) {
            SearchConstants.languageCodes = getJsonFromFile(
                "language_codes.json",
                SearchConstants.getLanguageCodeGson(),
                SearchConstants.codesType)!!.toSortedMap()
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



    fun trackGetRequest() {
        loading.show()
        viewModel.modelResponse.observe(this, Observer { response ->
            if(response.isSuccessful){
                val responseItems = response.body()
                if (responseItems != null) {
                    (newsRecycler.adapter as NewsViewRecyclerView).updateList(responseItems)
                    updateNumArticlesFound(responseItems.size)
                }
            }else {
                Log.d("Response not successful", response.code().toString())
            }
            loading.hide()
        })
    }

    override fun searchHeadlines(params: HashMap<String, String>) {
        viewModel.getHeadlinesPost(params)
        trackGetRequest()
    }

    override fun search(params: HashMap<String, String>) {
        viewModel.getEverythingPost(params)
        trackGetRequest()
    }

    private fun updateNumArticlesFound(num: Int) {
        val numArticleText = findViewById<TextView>(R.id.numArticles)
        if(num == 0) {
            numArticleText.text = "No articles found"
        }
        else{
            numArticleText.text = "Number of articles found: $num"
        }
    }
}
