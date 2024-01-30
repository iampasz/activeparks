package com.app.activeparks.ui.routeActive.fragments.detailsRouteActive

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.activeparks.data.model.routeActive.RouteActiveResponse
import com.app.activeparks.data.useCase.routeActive.RouteActiveStateUseCase
import kotlinx.coroutines.launch

class DetailsRouteActiveViewModel(
    private val routeActiveStateUseCase: RouteActiveStateUseCase
) : ViewModel() {

    var routeActive: MutableLiveData<RouteActiveResponse> = MutableLiveData()

    fun getRouteActive(id: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                routeActiveStateUseCase.getRouteActive(id)
            }.onSuccess { response ->
                response?.let { item ->
                    routeActive.value = item
                }
            }
        }
    }

}