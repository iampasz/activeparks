package com.app.activeparks.ui.userProfile.statisticFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.db.entity.ActiveEntity
import com.app.activeparks.data.model.activity.ActivityItemResponse
import com.app.activeparks.data.useCase.saveActivity.SaveActivityUseCase
import com.app.activeparks.data.useCase.statistics.StatisticsUseCase
import com.app.activeparks.ui.userProfile.model.ActivityInfoStatisticTrainingItem
import com.app.activeparks.ui.userProfile.model.StatisticResponseMapper
import kotlinx.coroutines.launch

/**
 * Created by O.Dziuba on 25.12.2023.
 */
class StatisticViewModel(
    private val saveActivityUseCase: SaveActivityUseCase,
    private val statisticsUseCase: StatisticsUseCase
): ViewModel() {

    val activities: MutableLiveData<List<ActivityItemResponse>> = MutableLiveData()
    val update: MutableLiveData<Boolean> = MutableLiveData(false)
    var firstListStatistic = ActivityInfoStatisticTrainingItem.getActivityInfoItem()
    var secondListStatistic: List<ActivityInfoStatisticTrainingItem> = listOf()

    fun getActivities() {
        viewModelScope.launch {
            kotlin.runCatching {
                saveActivityUseCase.getWorkoutsActivity()
            }.onSuccess {
                it?.let {
                    activities.value = it.items
                }
            }.onFailure {
                val s = it
            }
        }
    }

    fun getStatistic(from: String, to: String, isFirst: Boolean = true) {
        viewModelScope.launch {
            kotlin.runCatching {
                statisticsUseCase.getStatistics(from, to)
            }.onSuccess { response ->
                if (isFirst) {
                    response?.let {
                        firstListStatistic = StatisticResponseMapper.map(it)
                    }
                } else {
                    response?.let {
                        secondListStatistic = StatisticResponseMapper.map(it)
                    }

                }
                update.value = true
            }.onFailure {
                val s = it
            }
        }
    }
}