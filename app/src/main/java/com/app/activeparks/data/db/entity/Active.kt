package com.app.activeparks.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.activeparks.ui.active.model.Feeling
import com.app.activeparks.ui.active.model.InfoItem

/**
 * Created by O.Dziuba on 09.11.2023.
 */
@Entity(tableName = "active")
data class Active(
    @Embedded(prefix = "location_")
    var location: InfoItem,
    @Embedded(prefix = "weather_")
    var weather: InfoItem,
    @ColumnInfo(name = "date_time")
    var dateTime: String,
    @ColumnInfo(name = "title_activity")
    var titleActivity: String,
    @ColumnInfo(name = "description_activity")
    var descriptionActivity: String,
    @Embedded(prefix = "feeling_")
    var feeling: Feeling
) {
    @PrimaryKey(autoGenerate = true)
    var keyId: Long = 0
}