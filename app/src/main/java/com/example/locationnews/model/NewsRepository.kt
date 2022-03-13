package com.example.locationnews.model

class NewsRepository {

    suspend fun getEverythingPost(params: HashMap<String, String>) : ArrayList<NewsGet> {
        params["apiKey"] = API_KEY
        return NewsUtils.api.getEverything(params)
    }

    suspend fun getHeadlinesPost(params: HashMap<String, String>) : ArrayList<NewsGet> {
        params["apiKey"] = API_KEY
        return NewsUtils.api.getHeadlines(params)
    }
}