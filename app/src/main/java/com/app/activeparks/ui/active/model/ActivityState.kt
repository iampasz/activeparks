package com.app.activeparks.ui.active.model

/**
 * Created by O.Dziuba on 30.10.2023.
 */
data class ActivityState(
    var activityType: TypeOfActivity  = TypeOfActivity.getTypeOfActivity()[0]
)
