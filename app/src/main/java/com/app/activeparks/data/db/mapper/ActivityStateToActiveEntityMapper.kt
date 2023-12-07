package com.app.activeparks.data.db.mapper

import com.app.activeparks.data.db.entity.ActiveEntity
import com.app.activeparks.ui.active.model.ActivityInfoTrainingItem
import com.app.activeparks.ui.active.model.ActivityState
import com.app.activeparks.ui.active.model.ActivityTime
import com.app.activeparks.ui.active.model.CurrentActivity
import com.app.activeparks.ui.active.model.StartInfo
import com.app.activeparks.util.extention.getListForBack
import com.app.activeparks.util.extention.timeToSeconds
import java.util.UUID

/**
 * Created by O.Dziuba on 16.11.2023.
 */
class ActivityStateToActiveEntityMapper {
    companion object {
        fun map(
            startInfo: StartInfo,
            currentActivity: CurrentActivity,
            activityState: ActivityState,
            activityInfoItems: List<ActivityInfoTrainingItem>,
            activityTime: ActivityTime
        ): ActiveEntity {
            return ActiveEntity(
                UUID.randomUUID().toString(),
                "",
                currentActivity.descriptionActivity ?: "",
                activityTime.time,
                activityTime.startsAt,
                activityTime.finishesAt,
                activityInfoItems[6].number.toDouble().toInt(),
                activityInfoItems[7].number.toDouble().toInt(),
                activityInfoItems[8].number.toDouble().toInt(),
                activityInfoItems[2].number.toDouble().toInt(),
                activityInfoItems[1].number.toDouble().toInt(),
                activityInfoItems[9].number.timeToSeconds(),
                activityInfoItems[9].number,
                activityInfoItems[4].number.toDouble().toLong(),
                activityState.weatherIcon.toString(),
                startInfo.weather.unit,
                activityInfoItems[13].number.toDouble().toInt(),
                currentActivity.feeling?.id ?: 0,
                startInfo.weather.unit,
                activityState.activeRoad.getListForBack(),
                currentActivity.uri.toString(),
                listOf(),
                activityInfoItems[3].number,
                if (currentActivity.titleActivity.isNullOrEmpty()) {
                    currentActivity.dateTime ?: ""
                } else {
                    currentActivity.titleActivity ?: ""
                },
                currentActivity.descriptionActivity ?: "",
                activityState.activityType.id.toString(),
                activityState.activityTypeOutside.id,
                startInfo.startPoint.unit,
                "",
                "",
                activityState.activeRoad.getListForBack()
            )
        }
    }
}