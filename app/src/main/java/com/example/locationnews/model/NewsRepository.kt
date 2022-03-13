package com.example.locationnews.model

class NewsRepository {

    suspend fun getEverythingPost(params: HashMap<String, String>) : ArrayList<NewsGet> {
        return NewsUtils.api.getEverything(params)
    }
}