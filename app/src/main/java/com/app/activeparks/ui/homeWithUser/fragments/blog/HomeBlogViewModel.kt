package com.app.activeparks.ui.homeWithUser.fragments.blog

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.activeparks.data.model.news.News
import com.app.activeparks.data.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by O.Dziuba on 18.12.2023.
 */
class HomeBlogViewModel(
    private val repository: Repository
) : ViewModel() {

    val newsList = MutableLiveData<News>()

    @SuppressLint("CheckResult")
    fun getNews() {
        repository.news(5).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result: News -> newsList.setValue(result) }

    }
}