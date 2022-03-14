package com.example.locationnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.locationnews.model.*
import com.google.gson.Gson
import okhttp3.internal.wait
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Type

lateinit var viewModel: NewsViewModel
lateinit var newsRecycler: RecyclerView


class MainActivity : AppCompatActivity(), SearchInterface{

    private lateinit var loading: LoadingDialog
    private val MAX_NUM_PAGES = 15

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

    fun trackGetRequest(listener: (HashMap<String, String>) -> Unit, params: HashMap<String, String>) {
        loading.show()
        val items = mutableListOf<NewsGet>()
        var index = 1
        params["page"] = index.toString()
        listener(params)
        viewModel.modelResponse.observe(this, Observer { response ->
            if(response.isSuccessful && index <= MAX_NUM_PAGES){
                val responseItems = response.body()
                if (responseItems != null) {
                        items.addAll(responseItems.toMutableList())
                    index++
                    params["page"] = index.toString()
                    Log.d("At page ", "" + index)
                    Thread.sleep(500)
                    listener(params)
                }
                else{
                    (newsRecycler.adapter as NewsViewRecyclerView).updateList(items)
                    updateNumArticlesFound(items.size)
                }
            }else {
                (newsRecycler.adapter as NewsViewRecyclerView).updateList(items)
                updateNumArticlesFound(items.size)
                Log.d("Response not successful", response.code().toString())
            }
            Handler().post {
                loading.hide()
            }

        })
    }

    override fun searchHeadlines(params: HashMap<String, String>) {
        trackGetRequest(viewModel::getHeadlinesPost, params)
    }

    override fun search(params: HashMap<String, String>) {
        trackGetRequest(viewModel::getEverythingPost, params)
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
