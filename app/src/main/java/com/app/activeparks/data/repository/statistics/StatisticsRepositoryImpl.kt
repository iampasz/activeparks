package com.app.activeparks.data.repository.statistics

import com.app.activeparks.data.model.statistic.StatisticResponse
import com.app.activeparks.data.network.NetworkManager

/**
 * Created by O.Dziuba on 29.12.2023.
 */
class StatisticsRepositoryImpl(
    private val networkManager: NetworkManager
)  : StatisticsRepository {
    override suspend fun getStatistics(from: String, to: String): StatisticResponse? {
        return networkManager.getStatistics(from, to)
    }
}