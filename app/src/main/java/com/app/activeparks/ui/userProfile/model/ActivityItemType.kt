package com.app.activeparks.ui.userProfile.model

import com.app.activeparks.data.model.activity.ActivityItemResponse

/**
 * Created by O.Dziuba on 17.01.2024.
 */
sealed class ActivityItemType {
    data class Header(val year: String) : ActivityItemType()
    data class Item(val activity: ActivityItemResponse) : ActivityItemType()


}
