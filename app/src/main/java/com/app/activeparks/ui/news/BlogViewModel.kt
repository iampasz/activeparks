package com.app.activeparks.ui.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.news.ItemNews
import com.app.activeparks.data.model.news.NewsListResponse
import com.app.activeparks.data.useCase.news.NewsUseCase
import kotlinx.coroutines.launch

class BlogViewModel (
    private val newsUseCase: NewsUseCase
) : ViewModel() {

    val newsList = MutableLiveData<NewsListResponse>()
    val newsDetails = MutableLiveData<ItemNews>()
    val newsClubDetails = MutableLiveData<ItemNews>()

    fun getNews() {
        viewModelScope.launch {
            kotlin.runCatching {
                newsUseCase.getNews()

            }.onSuccess { response ->
                response?.let {
                    newsList.value = it
                }
            }
        }
    }

    fun getNewsDetails(id:String) {
        viewModelScope.launch {
            kotlin.runCatching {
                newsUseCase.getNewsDetails(id)

            }.onSuccess { response ->
                response?.let {
                    newsDetails.value = it
                }
            }
        }
    }

    fun getClubNewsDetails(club:String, id:String) {
        viewModelScope.launch {
            kotlin.runCatching {
                newsUseCase.getClubNewsDetails(club, id)

            }.onSuccess { response ->
                response?.let {
                    newsClubDetails.value = it
                }
            }
        }
    }
}