package com.app.activeparks.data.repository.news

import com.app.activeparks.data.model.news.ItemNews
import com.app.activeparks.data.model.news.NewsListResponse

interface NewsRepository {
    suspend fun getNews(): NewsListResponse?
    suspend fun getNewsDetails(id:String): ItemNews?
    suspend fun getClubNewsDetails(club:String, id:String): ItemNews?
}
