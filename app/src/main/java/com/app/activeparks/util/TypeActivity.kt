package com.app.activeparks.util

import android.content.Context
import com.app.activeparks.data.storage.Preferences
import com.app.activeparks.ui.active.model.TypeOfActivity
import com.technodreams.activeparks.R

class TypeActivity {
    companion object {
        fun getTypeOfActivityName(context: Context, type: String): String {
            val sharedPreferences =  Preferences(context)
            if (sharedPreferences.getDictionarie() != null) {
                for (baseDictionaries in sharedPreferences.getDictionarie().getClubTypes()) {
                    if (baseDictionaries.id == type) {
                        return baseDictionaries.title
                    }
                }
            }
            return "Біг";
        }
        fun getTypeOfActivityIcon(type: String): Int {
            return when (type) {
                "83a70a99-f4e2-4b2d-b004-0ad75e1dbb43" -> R.drawable.ic_at_run
                "eb1f23b9-a872-43ef-8462-a513504af111" -> R.drawable.ic_at_at_home
                "625d2bd5-f045-4341-a2e1-e53ef1f45104" -> R.drawable.ic_at_bicycle
                "e8b243de-fba4-4b16-bb6b-30a38e227328" -> R.drawable.ic_at_scandinavian_walk
                else -> R.drawable.ic_at_walk
            }
        }
    }
}