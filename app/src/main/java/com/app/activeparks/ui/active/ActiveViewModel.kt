package com.app.activeparks.ui.active

import android.location.Location
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.registration.PulseZoneRequest
import com.app.activeparks.data.storage.Preferences
import com.app.activeparks.data.useCase.activeState.ActivityStateUseCase
import com.app.activeparks.data.useCase.pauseActivity.PauseActivityUseCase
import com.app.activeparks.data.useCase.registration.UserUseCase
import com.app.activeparks.data.useCase.weatehr.WeatherUseCase
import com.app.activeparks.ui.active.model.ActivityState
import com.technodreams.activeparks.R
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ActiveViewModel(
    private val activityStateUseCase: ActivityStateUseCase,
    private val weatherUseCase: WeatherUseCase,
    private val userUseCase: UserUseCase,
    private val pauseActivityUseCase: PauseActivityUseCase,
    preferences: Preferences
) : ViewModel() {

    val navigate = MutableLiveData<Fragment?>()
    var location: Location? = null

    var activityState = ActivityState()

    var heartRateList = ArrayList<Int>()
    var updateActivityInfoTrainingItem = MutableLiveData(false)
    val updateUI: MutableLiveData<Boolean> = MutableLiveData(false)
    val checkLocation: MutableLiveData<Boolean> = MutableLiveData(true)
    val saved: MutableLiveData<Boolean> = MutableLiveData(false)
    val updateMap: MutableLiveData<Boolean> = MutableLiveData(false)
    val updateWeather: MutableLiveData<Boolean> = MutableLiveData(false)
    val pulseZoneRequest: MutableLiveData<PulseZoneRequest> = MutableLiveData()
    val currentPulse: MutableLiveData<Boolean> = MutableLiveData(false)
    var pulseZone = PulseZoneRequest()

    var isAuth = false

    init {
        loadActiveState()
        getWeight()
        isAuth = !preferences.token.isNullOrEmpty()

    }

    fun isActivityPause() {
        viewModelScope.launch {
            kotlin.runCatching {
                pauseActivityUseCase.getActive()
            }.onSuccess {
                it?.apply {
                    activityState = this
                    activityState.isPause = true
                    activityState.isTrainingStart = true
                    updateUI.value = true
                }
            }
        }
    }
    fun pauseActivity() {
        viewModelScope.launch {
            pauseActivityUseCase.insert(activityState)
        }
    }
    fun deletePauseActivity() {
        viewModelScope.launch {
            pauseActivityUseCase.delete()
        }
    }

    private fun getWeight() {
        viewModelScope.launch {
            kotlin.runCatching {
                userUseCase.getUser()
            }.onSuccess {
                it?.let {
                    activityState.weight = it.weight ?: 90
                    it.birthday?.let { bDay -> activityState.age = calculateAge(bDay) }
                }
            }
        }
    }

    private fun calculateAge(dateOfBirth: String): Int {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val birthDate = LocalDate.parse(dateOfBirth, formatter)
        val currentDate = LocalDate.now()
        val age = currentDate.year - birthDate.year
        if (currentDate.dayOfYear < birthDate.dayOfYear) {
            return age - 1
        }

        return age
    }

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

    fun getPulseZone() {
        if (isAuth) {
            viewModelScope.launch {
                kotlin.runCatching {
                    userUseCase.getHeartRateZones()
                }.onSuccess { request ->
                    request?.upperBorder?.let {
                        pulseZoneRequest.value = if (it == 0) {
                            PulseZoneRequest.getAutoPulseZone(
                                activityState.age,
                                pulseZoneRequest.value?.pausePulse ?: 60
                            )
                        } else {
                            request
                        }
                    }
                }
            }
        }

    }

    fun savePulseZone() {
        if (isAuth) {
            viewModelScope.launch {
                kotlin.runCatching {
                    userUseCase.setHeartRateZones(pulseZone)
                }
            }
        }
    }

    fun updatePulses() {
        if (activityState.minPulse > activityState.currentPulse) {
            activityState.minPulse = activityState.currentPulse
            activityState.activityInfoItems[8].number = activityState.minPulse.toString()
            activityState.activityPulseItems[0].number = activityState.minPulse.toString()
        }

        if (activityState.maxPulse < activityState.currentPulse) {
            activityState.maxPulse = activityState.currentPulse
            activityState.activityInfoItems[7].number = activityState.maxPulse.toString()
            activityState.activityPulseItems[2].number = activityState.maxPulse.toString()
        }
        saveAveragePulse()

    }

     fun saveAveragePulse() {

         activityState.activityPulseItems[1].number = heartRateList.size.toString()
         activityState.activityInfoItems[6].number = heartRateList.size.toString()

        // Log.i("AVERAGERER", "averagePulse $averagePulse")

    }
}