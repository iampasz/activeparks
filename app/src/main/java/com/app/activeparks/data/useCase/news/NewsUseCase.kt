package com.app.activeparks.data.useCase.news

import com.app.activeparks.data.model.news.ItemNews
import com.app.activeparks.data.model.news.NewsListResponse

interface NewsUseCase {
    suspend fun getNews():NewsListResponse?
    suspend fun getNewsDetails(id:String):ItemNews?
    suspend fun getClubNewsDetails(club: String, id:String):ItemNews?
}