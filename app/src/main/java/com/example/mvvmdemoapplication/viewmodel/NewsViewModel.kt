package com.example.mvvmdemoapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmdemoapplication.model.Article
import com.example.mvvmdemoapplication.model.NewsResponse
import com.example.mvvmdemoapplication.repository.NewsRepository
import com.example.mvvmdemoapplication.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

     var headlinesNewsPage = 1
    private var headlinesNewsResponse: NewsResponse? = null
    val headlineNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

     val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
     var searchNewsPage = 1
    private var searchNewsResponse: NewsResponse? = null

    val favouriteValue = MutableLiveData<Boolean>()

    fun getHeadlineNews(countryCode: String) = viewModelScope.launch {
        headlineNews.postValue(Resource.Loading())
        val response = newsRepository.getHeadlines(countryCode, headlinesNewsPage)
        headlineNews.postValue(handleHeadlinesResponse(response))
    }

    private fun handleHeadlinesResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                headlinesNewsPage++
                if (headlinesNewsResponse == null) {
                    headlinesNewsResponse = resultResponse
                } else {
                    val oldArticles = headlinesNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(headlinesNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun searchForNews(searchQuery: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = newsRepository.searchForNews(searchQuery, searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        favouriteValue.postValue(true)
        newsRepository.insertArticle(article)
    }

    fun getSavedNews() = newsRepository.getSavedArticles()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        favouriteValue.postValue(false)
        newsRepository.deleteArticle(article)
    }

    fun setFavouriteValue(fav:Boolean) = viewModelScope.launch {
        favouriteValue.postValue(fav)
    }
}