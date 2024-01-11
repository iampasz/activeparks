package com.app.activeparks.ui.active.model

import java.io.File

/**
 * Created by O.Dziuba on 08.11.2023.
 */
data class CurrentActivity(
    var location: InfoItem? = null,
    var weather: InfoItem? = null,
    var dateTime: String? = null,
    var titleActivity: String? = null,
    var descriptionActivity: String? = null,
    var feeling: Feeling? = null,
    var file: File? = null
)
