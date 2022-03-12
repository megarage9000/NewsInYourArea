package com.example.locationnews

import android.util.Log
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
    suspend fun getEverything(@QueryMap params: HashMap<String, String>) : Response<ResponseBody>

}

//fun getNews(params: HashMap<String, String>) {
//    params["apiKey"] = API_KEY;
//
//    // Create retrofit
//    val retrofit = Retrofit.Builder()
//        .baseUrl(NEWS_API)
//        .build()
//
//    val service = retrofit.create(NewsApi::class.java);
//
//    // Starting response (asynchronously)
//    CoroutineScope(Dispatchers.IO).launch {
//
//        val response = service.getEverything(params)
//
//        withContext(Dispatchers.Main) {
//            if(response.isSuccessful) {
//
//                val gson = GsonBuilder().setPrettyPrinting().create()
//                val result = gson.toJson(
//                    JsonParser.parseString(
//                        response.body()?.string()
//                    )
//                )
//                Log.d("Response Success: ", result)
//            }
//            else {
//                Log.e("RETROFIT FAIL: ", response.code().toString())
//            }
//        }
//    }
//}