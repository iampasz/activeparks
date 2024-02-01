package com.app.activeparks.ui.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.activeparks.data.model.routeActive.RouteActiveResponse
import com.app.activeparks.data.model.sportsgrounds.Sportsgrounds
import com.app.activeparks.data.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint

class MapsViewModel : ViewModel(), MapEventsReceiver {

    val sportsgroundsList = MutableLiveData<Sportsgrounds>()
    val routeActiveList = MutableLiveData<List<RouteActiveResponse>>()

    private val apiRepository: Repository

    private var lat = 30.51814
    private var lon = 50.44812
    private var myLat = 30.51814
    private var myLon = 50.44812

    var isFilterActiveRoute = false
    var isFilterActivePark = true

    var isFilterList = false

    init {
        apiRepository = Repository()
    }

    val sportsground: LiveData<Sportsgrounds>
        get() = sportsgroundsList

    fun getSportsgroundList(radius: Int) {
        apiRepository.getSportsgroundsAndRoute("" + radius, "" + lat, "" + lon)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result: Sportsgrounds ->
                    sportsgroundsList.value = result
                    routeActiveList.value = result.activeRouter
                }
            ) { error: Throwable? -> error?.message?.let { Log.d("TESTERR", it) } }
    }

    fun setUpdateSportsgroundList(radius: Int, lat: Double, lon: Double) {
        this.lat = lon
        this.lon = lat
        getSportsgroundList(radius)
    }

    fun setGeolocation(lat: Double, lon: Double) {
        this.lat = lon
        this.lon = lat
    }

    fun setMylocation(lat: Double, lon: Double) {
        myLat = lon
        myLon = lat
    }

    override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
        return false
    }

    override fun longPressHelper(p: GeoPoint): Boolean {
        return false
    }
}