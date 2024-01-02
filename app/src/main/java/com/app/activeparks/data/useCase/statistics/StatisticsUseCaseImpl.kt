package com.app.activeparks.data.useCase.statistics

import com.app.activeparks.data.model.statistic.StatisticResponse
import com.app.activeparks.data.repository.statistics.StatisticsRepository

/**
 * Created by O.Dziuba on 29.12.2023.
 */
class StatisticsUseCaseImpl(
    private val statisticsRepository: StatisticsRepository
) : StatisticsUseCase {
    override suspend fun getStatistics(from: String, to: String): StatisticResponse? {
        return statisticsRepository.getStatistics(from, to)
    }
}