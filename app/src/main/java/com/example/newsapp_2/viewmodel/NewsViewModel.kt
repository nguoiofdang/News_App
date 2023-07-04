package com.example.newsapp_2.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp_2.NewsApplication
import com.example.newsapp_2.models.Article
import com.example.newsapp_2.models.NewsResponse
import com.example.newsapp_2.repository.NewsRepository
import com.example.newsapp_2.util.Constant.COUNTRY_CODE
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    private val application: Application,
    private val repository: NewsRepository
) : AndroidViewModel(application) {

    val breakingNewsList: MutableLiveData<NewsResponse?> = MutableLiveData()
    val searchNewsList: MutableLiveData<NewsResponse?> = MutableLiveData()

    init {
        getBreakingNew()
    }

    private fun getBreakingNew() = viewModelScope.launch {
        safeBreakingNewsCall()
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): NewsResponse? {
        response.body()?.let { newsResponse ->
            if (response.isSuccessful){
                return newsResponse
            }
        }
        Toast.makeText(application, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
        return null
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): NewsResponse? {
        response.body()?.let { newsResponse ->
            if (response.isSuccessful){
                return newsResponse
            }
        }
        Toast.makeText(application, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
        return null
    }

    fun insertArticle(article: Article) = viewModelScope.launch {
        repository.insertArticle(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }

    val getAllArticles = repository.getAllArticles()

    private suspend fun safeSearchNewsCall(searchQuery: String) {
        try {
            if(hasInternetConnection()) {
                val response = repository.searchNews(searchQuery)
                searchNewsList.postValue(handleSearchNewsResponse(response))
            }
            Toast.makeText(application, "No Internet connect", Toast.LENGTH_SHORT).show()
        } catch (t:Throwable) {
            when(t) {
                is IOException -> Toast.makeText(application, "Network failure", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(application, "Conversion Error", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private suspend fun safeBreakingNewsCall() {
        try {
            if(hasInternetConnection()) {
                val response = repository.getBreakingNews(COUNTRY_CODE)
                breakingNewsList.postValue(handleBreakingNewsResponse(response))
            }
           Toast.makeText(application, "No Internet connect", Toast.LENGTH_SHORT).show()
        } catch (t:Throwable) {
            when(t) {
                is IOException -> Toast.makeText(application, "Network failure", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(application, "Conversion Error", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetWork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetWork) ?: return false
        return when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}