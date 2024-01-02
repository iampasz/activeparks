package com.app.activeparks.data.repository.statistics

import com.app.activeparks.data.model.statistic.StatisticResponse

/**
 * Created by O.Dziuba on 29.12.2023.
 */
interface StatisticsRepository {
    suspend fun getStatistics(from: String, to: String): StatisticResponse?
}
