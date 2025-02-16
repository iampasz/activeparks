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
import com.app.activeparks.util.extention.toInfo
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
                    activityState.weight = it.weight ?: 90.100
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
                it?.let {
                    //TODO need change
                    val list = activityState.activeRoad
                    val type = activityState.typeOfTraining
                    val idRout = activityState.activeRoadId
                    activityState = it
                    activityState.activeRoad.clear()
                    activityState.activeRoad.addAll(list)
                    activityState.typeOfTraining = type
                    activityState.activeRoadId = idRout
                }
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
        if (activityState.activityRoad.isNotEmpty()) {
            getWeather(activityState.activityRoad.first())
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
                    weatherIcon = response.weather.first().icon
                    temperature = response.main.temp.toInfo()
                }
                updateWeather.value = true
            }.onFailure {
                updateWeather.value = true
            }
        }
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

    private fun saveAveragePulse() {
        val currentSize = heartRateList.size
        val sum = heartRateList.sum()
        val averageValue = sum / currentSize
        activityState.activityPulseItems[1].number = averageValue.toString()
        activityState.activityInfoItems[6].number = averageValue.toString()
    }
}