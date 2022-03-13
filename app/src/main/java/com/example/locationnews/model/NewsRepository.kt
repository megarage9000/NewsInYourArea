package com.example.locationnews.model

import retrofit2.Response

class NewsRepository {

    suspend fun getEverythingPost(params: HashMap<String, String>) : Response<MutableList<NewsGet>> {
        params["apiKey"] = API_KEY
        return NewsUtils.api.getEverything(params)
    }

    suspend fun getHeadlinesPost(params: HashMap<String, String>) : Response<MutableList<NewsGet>> {
        params["apiKey"] = API_KEY
        return NewsUtils.api.getHeadlines(params)
    }
}