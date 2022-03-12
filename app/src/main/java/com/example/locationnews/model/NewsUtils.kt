package com.example.locationnews.model

import com.example.locationnews.NEWS_API
import com.example.locationnews.NewsApi
import retrofit2.Retrofit

object NewsUtils {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(NEWS_API)
            .build()
    }
    val api: NewsApi by lazy {
        retrofit.create(NewsApi::class.java)
    }
}