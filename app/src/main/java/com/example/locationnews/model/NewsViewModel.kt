package com.example.locationnews.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: NewsRepository) : ViewModel(){

    val modelResponse: MutableLiveData<ArrayList<NewsGet>> = MutableLiveData()

    fun getEverythingPost(params : HashMap<String, String>) {
        viewModelScope.launch {
            val response = repository.getEverythingPost(params)
            modelResponse.value = response
        }

    }
}

class NewsViewModelFactory(private val repository: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(repository) as T
    }
}