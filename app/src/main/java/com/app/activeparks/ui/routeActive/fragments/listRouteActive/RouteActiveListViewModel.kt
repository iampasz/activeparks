package com.app.activeparks.ui.routeActive.fragments.listRouteActive

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.registration.User
import com.app.activeparks.data.model.routeActive.RouteActiveResponse
import com.app.activeparks.data.model.track.TrackResponse
import com.app.activeparks.data.storage.Preferences
import com.app.activeparks.data.useCase.registration.UserUseCase
import com.app.activeparks.data.useCase.routeActive.RouteActiveStateUseCase
import com.app.activeparks.data.useCase.trackState.TrackStateUseCase
import com.app.activeparks.ui.userProfile.model.PhotoType
import com.app.activeparks.util.extention.toInfo
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint


class RouteActiveListViewModel(
    private val routeActiveStateUseCase: RouteActiveStateUseCase
) : ViewModel() {

    val routeActiveList = MutableLiveData<List<RouteActiveResponse>>()
    @SuppressLint("CheckResult")
    fun getRouteActivesList() {
        viewModelScope.launch {
            kotlin.runCatching {
                routeActiveStateUseCase.getRouteActives("")
            }.onSuccess { response ->
                response?.items?.let { items ->
                    routeActiveList.value = items
                }
            }
        }
    }
    @SuppressLint("CheckResult")
    fun getFindRouteActivesList(name: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                routeActiveStateUseCase.getRouteActives(name)
            }.onSuccess { response ->
                response?.items?.let { items ->
                    routeActiveList.value = items
                }
            }
        }
    }
}
