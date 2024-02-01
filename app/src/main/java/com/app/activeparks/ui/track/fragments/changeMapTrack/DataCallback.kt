package com.app.activeparks.ui.track.fragments.changeMapTrack

import com.app.activeparks.data.model.track.PointsTrack

interface DataCallback {
    fun onDataReceived(data: List<PointsTrack>)
}