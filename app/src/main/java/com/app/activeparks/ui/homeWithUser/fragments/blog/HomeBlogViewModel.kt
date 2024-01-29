package com.app.activeparks.ui.homeWithUser.fragments.blog

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.news.NewsListResponse
import com.app.activeparks.data.useCase.news.NewsUseCase
import kotlinx.coroutines.launch

/**
 * Created by O.Dziuba on 18.12.2023.
 */
class HomeBlogViewModel(
    private val newsUseCase: NewsUseCase
) : ViewModel() {

    val newsList = MutableLiveData<NewsListResponse>()

    fun getNews() {
        viewModelScope.launch {
            kotlin.runCatching {
                newsUseCase.getNews()

            }.onSuccess { response ->
                response?.let {
                    Log.i("TEST_NEWS", "${it.items} hello")
                    newsList.value = it
                }
            }
        }
    }
}