package com.example.locationnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.locationnews.model.API_KEY
import com.example.locationnews.model.NewsRepository
import com.example.locationnews.model.NewsViewModel
import com.example.locationnews.model.NewsViewModelFactory

lateinit var viewModel: NewsViewModel

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = NewsRepository()
        val viewModelFactory = NewsViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewsViewModel::class.java)

        val params = hashMapOf<String, String>(
            "q" to "apple",
            "langauge" to "en",
            "apiKey" to API_KEY
        )

        viewModel.getEverythingPost(params)
        viewModel.modelResponse.observe(this, Observer { response ->
            for(value in response) {
                Log.d("------", "")
                Log.d("Publisher", value.publisher)
                Log.d("Title", value.title)
                Log.d("Description", value.description)
                Log.d("URL", value.urlToPage)
                Log.d("Published At", value.publishedAt)
            }
        })
    }
}
