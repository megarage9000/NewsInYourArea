package com.example.locationnews.model

import android.util.Log
import com.example.locationnews.model.NewsGet
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.QueryMap

const val NEWS_API = "https://newsapi.org/v2/"
const val API_KEY = "b418a73b2686469bbd9b60b8d583ed68"

// Tutorial content: https://johncodeos.com/how-to-make-post-get-put-and-delete-requests-with-retrofit-using-kotlin/
interface NewsApi {
    // Using extra parameters
    @GET("everything")
    suspend fun getEverything(@QueryMap params: HashMap<String, String>) : ArrayList<NewsGet>
}