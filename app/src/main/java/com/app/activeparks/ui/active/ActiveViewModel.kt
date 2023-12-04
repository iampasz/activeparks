package com.app.activeparks.ui.active

import android.location.Location
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.useCase.activeState.ActivityStateUseCase
import com.app.activeparks.data.useCase.weatehr.WeatherUseCase
import com.app.activeparks.ui.active.model.ActivityInfoTrainingItem
import com.app.activeparks.ui.active.model.ActivityState
import com.app.activeparks.ui.active.model.ActivityTime
import com.app.activeparks.ui.active.model.InfoItem
import com.technodreams.activeparks.R
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

class ActiveViewModel(
    private val activityStateUseCase: ActivityStateUseCase,
    private val weatherUseCase: WeatherUseCase
) : ViewModel() {

    val navigate = MutableLiveData<Fragment?>()
    var location: Location? = null

    var activityDuration = 0

    var activityState = ActivityState()
    var activityTime = ActivityTime()
    var activityInfoItems: List<ActivityInfoTrainingItem> =
        ActivityInfoTrainingItem.getActivityInfoItem()
    var activityPulseItems: List<InfoItem> = InfoItem.getPulseInfo()

    var updateActivityInfoTrainingItem = MutableLiveData(false)
    val updateUI: MutableLiveData<Boolean> = MutableLiveData(false)
    val checkLocation: MutableLiveData<Boolean> = MutableLiveData(true)
    val saved: MutableLiveData<Boolean> = MutableLiveData(false)
    val updateMap: MutableLiveData<Boolean> = MutableLiveData(false)
    val updateWeather: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        loadActiveState()
    }

    //TODO change after completing the registration flow
    fun getWeight() = 70.0

    fun loadActiveState() {
        viewModelScope.launch {
            kotlin.runCatching {
                activityStateUseCase.getActivityState()
            }.onSuccess {
                it?.let { activityState = it }
                updateUI.value = true
            }
        }
    }

    fun saveActiveState() {
        viewModelScope.launch {
            kotlin.runCatching {
                activityStateUseCase.saveActivityState(activityState)
            }
        }
    }

    fun navigateTo(fragment: Fragment) {
        navigate.value = fragment
    }

    fun getWeather() {
        if (activityState.activeRoad.isNotEmpty()) {
            getWeather(activityState.activeRoad.first())
        } else if (location != null) {
            getWeather(GeoPoint(location?.latitude ?: 0.0, location?.longitude ?: 0.0))
        } else {
            updateWeather.value = true
        }
    }

    private fun getWeather(geoPoint: GeoPoint) {
        viewModelScope.launch {
            kotlin.runCatching {
                weatherUseCase.getWeather(
                    geoPoint.latitude,
                    geoPoint.longitude
                )
            }.onSuccess { response ->
                with(activityState) {
                    weather =
                        "${response.weather.first().description.replaceFirstChar { it.uppercase() }}, ${response.main.temp.toInt()} C"
                    weatherIcon = getWeatherIconResource(response.weather.first().icon)
                }
                updateWeather.value = true
            }.onFailure {
                updateWeather.value = true
            }
        }
    }

    private fun getWeatherIconResource(iconName: String): Int {
        val resourceId = when (iconName.substring(0, 2)) {
            "01" -> R.drawable.ic_01
            "02" -> R.drawable.ic_02
            "03" -> R.drawable.ic_03
            "04" -> R.drawable.ic_03
            "09" -> R.drawable.ic_09
            "10" -> R.drawable.ic_10
            "11" -> R.drawable.ic_11
            "13" -> R.drawable.ic_13
            "50" -> R.drawable.ic_50
            else -> R.drawable.default_weather_icon
        }
        return resourceId
    }

}