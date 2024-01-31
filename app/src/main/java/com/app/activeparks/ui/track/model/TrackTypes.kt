package com.app.activeparks.ui.track.model

import com.app.activeparks.data.model.user.UserRole
import com.technodreams.activeparks.R

class TrackTypes (
    val id: String,
    var title: String,
    var img: Int
) {

    companion object {
        fun getTrackTypes() = listOf(
            TrackTypes("83a70a99-f4e2-4b2d-b004-0ad75e1dbb43","Біг", R.drawable.ic_at_run),
            TrackTypes("eb1f23b9-a872-43ef-8462-a513504af111","В приміщенні", R.drawable.ic_at_at_home),
            TrackTypes("625d2bd5-f045-4341-a2e1-e53ef1f45104","Велоперегони", R.drawable.ic_at_bicycle),
            TrackTypes("e8b243de-fba4-4b16-bb6b-30a38e227328","Скандинавська ходьба", R.drawable.ic_at_scandinavian_walk),
            TrackTypes("eb1f23b9-a872-43ef-8462-a513504af109","Ходьба", R.drawable.ic_at_walk),
        )
    }
}
