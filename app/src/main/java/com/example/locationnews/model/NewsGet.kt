package com.example.locationnews.model

data class NewsGet (
    val publisher: String,
    val title: String,
    val description: String,
    val urlToPage: String,
    val publishedAt: String
)