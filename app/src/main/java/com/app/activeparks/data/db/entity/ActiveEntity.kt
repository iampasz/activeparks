package com.app.activeparks.data.db.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.activeparks.ui.active.model.ActivityInfoTrainingItem
import com.app.activeparks.ui.active.model.Feeling
import com.app.activeparks.ui.active.model.InfoItem
import com.app.activeparks.ui.active.model.LevelOfActivity
import com.app.activeparks.ui.active.model.TypeOfActivity
import org.osmdroid.util.GeoPoint

/**
 * Created by O.Dziuba on 09.11.2023.
 */
@Entity(tableName = "active")
data class ActiveEntity(
    @Embedded(prefix = "location_")
    var location: InfoItem,
    @Embedded(prefix = "weather_")
    var weather: InfoItem,
    @ColumnInfo(name = "date_time")
    var dateTime: String,
    @ColumnInfo(name = "title_activity")
    var titleActivity: String,
    @ColumnInfo(name = "active_road")
    var activeRoad: List<GeoPoint>,
    @ColumnInfo(name = "description_activity")
    var descriptionActivity: String,
    @Embedded(prefix = "feeling_")
    var feeling: Feeling,
    @ColumnInfo(name = "activity_info_items")
    var activityInfoItems: List<ActivityInfoTrainingItem>,
    @ColumnInfo(name = "img_uri")
    var imgUri: Uri?
) {
    @PrimaryKey(autoGenerate = true)
    var keyId: Long = 0
}