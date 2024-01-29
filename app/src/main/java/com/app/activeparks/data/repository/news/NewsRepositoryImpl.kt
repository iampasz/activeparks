package com.app.activeparks.data.repository.news

import com.app.activeparks.data.model.news.ItemNews
import com.app.activeparks.data.model.news.NewsListResponse
import com.app.activeparks.data.network.NetworkManager

class NewsRepositoryImpl(
    private val networkManager: NetworkManager
) : NewsRepository {

    override suspend fun getNews(): NewsListResponse? {
        return networkManager.getNews()
    }

    override suspend fun getNewsDetails(id:String): ItemNews? {
        return networkManager.getNewsDetails(id)
    }

    override suspend fun getClubNewsDetails(club: String, id:String): ItemNews? {
        return networkManager.getClubNewsDetails(club, id)
    }


}