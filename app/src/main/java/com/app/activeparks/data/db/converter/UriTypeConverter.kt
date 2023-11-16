package com.app.activeparks.data.db.converter

import android.net.Uri
import androidx.room.TypeConverter

/**
 * Created by O.Dziuba on 16.11.2023.
 */
class UriTypeConverter {
    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        return uri?.toString()
    }

    @TypeConverter
    fun toUri(uriString: String?): Uri? {
        return uriString?.let { Uri.parse(it) }
    }
}