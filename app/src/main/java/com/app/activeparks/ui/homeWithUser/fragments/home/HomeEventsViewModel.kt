package com.app.activeparks.ui.homeWithUser.fragments.home

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.activeparks.data.model.sportevents.EventList
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.data.storage.Preferences
import com.app.activeparks.ui.event.interfaces.ResponseCallBack
import com.app.activeparks.ui.event.util.EventController
import com.google.gson.Gson

/**
 * Created by O.Dziuba on 18.12.2023.
 */
class HomeEventsViewModel(
    private val eventController: EventController,
    private val preferences: Preferences
) : ViewModel() {

    val eventList = MutableLiveData<List<ItemEvent>>()

    @SuppressLint("CheckResult")
    fun getEvents() {
        if (preferences.userName.isNotEmpty()) {

            val setDataResponseSuccessful = object : ResponseCallBack {
                override fun load(responseFromApi: String) {
                    val gson = Gson()
                    val eventList =
                        gson.fromJson(
                            responseFromApi,
                            EventList::class.java
                        )

                    val list: MutableList<ItemEvent> = mutableListOf()
                    list.addAll(eventList.items)
                    this@HomeEventsViewModel.eventList.value = list
                }

            }

            eventController.getMyEvents(setDataResponseSuccessful)
        } else {
            eventList.value = listOf()
        }
    }
}