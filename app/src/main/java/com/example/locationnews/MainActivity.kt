package com.example.locationnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.locationnews.model.*

lateinit var viewModel: NewsViewModel

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
}
