package com.app.activeparks.ui.userProfile.model

import com.app.activeparks.data.model.statistic.StatisticResponse
import com.app.activeparks.util.extention.toInfo

/**
 * Created by O.Dziuba on 29.12.2023.
 */
class StatisticResponseMapper {
    companion object {
        fun map(statisticResponse: StatisticResponse) : List<ActivityInfoStatisticTrainingItem> {
            val list = ActivityInfoStatisticTrainingItem.getActivityInfoItem()

            list[0].number = statisticResponse.dataSize.toString()
            list[1].number = statisticResponse.events.toString()
            list[2].number = statisticResponse.calories.toString()
            list[3].number = statisticResponse.bpm.toString()
            list[4].number = statisticResponse.minBpm.toString()
            list[5].number = statisticResponse.time.toString()
            list[6].number = statisticResponse.distance.toInfo()
            list[7].number = statisticResponse.temp.toString()
            list[8].number = statisticResponse.speed.toInfo()
            list[9].number = statisticResponse.speedMax.toInfo()
            list[10].number = statisticResponse.hight.toString()
            list[11].number = statisticResponse.altitudeMin.toString()
            list[12].number = statisticResponse.resultMinMax.toString()
            list[13].number = statisticResponse.resultMinMax.toString()

            return list
        }
    }
}