package com.app.activeparks.data.useCase.news

import com.app.activeparks.data.model.news.ItemNews
import com.app.activeparks.data.model.news.NewsListResponse
import com.app.activeparks.data.repository.news.NewsRepository

class NewsUseCaseImpl(
    private val newsRepository: NewsRepository
) : NewsUseCase {
    override suspend fun getNews(): NewsListResponse? {
       return newsRepository.getNews()
    }
    override suspend fun getNewsDetails(id:String): ItemNews? {
        return newsRepository.getNewsDetails(id)
    }

    override suspend fun getClubNewsDetails(club:String, id:String): ItemNews? {
        return newsRepository.getClubNewsDetails(club, id)
    }

}