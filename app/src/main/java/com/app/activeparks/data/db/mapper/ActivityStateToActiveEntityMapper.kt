package com.app.activeparks.data.db.mapper

import com.app.activeparks.data.db.entity.ActiveEntity
import com.app.activeparks.ui.active.model.ActivityInfoTrainingItem
import com.app.activeparks.ui.active.model.CurrentActivity
import com.app.activeparks.ui.active.model.Feeling
import com.app.activeparks.ui.active.model.StartInfo
import org.osmdroid.util.GeoPoint

/**
 * Created by O.Dziuba on 16.11.2023.
 */
class ActivityStateToActiveEntityMapper {
    companion object {
        fun map(
            startInfo: StartInfo,
            currentActivity: CurrentActivity,
            activeRoad: List<GeoPoint>,
            activityInfoItems: List<ActivityInfoTrainingItem>
        ): ActiveEntity {
            return ActiveEntity(
                location = startInfo.startPoint,
                weather = startInfo.weather,
                dateTime = currentActivity.dateTime ?: "",
                titleActivity = currentActivity.titleActivity ?: "",
                activeRoad = activeRoad,
                descriptionActivity = currentActivity.descriptionActivity ?: "",
                feeling = currentActivity.feeling ?: Feeling.getFiling().first(),
                activityInfoItems = activityInfoItems,
                currentActivity.uri
            )
        }
    }
}