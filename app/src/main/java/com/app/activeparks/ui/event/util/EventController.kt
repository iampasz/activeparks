package com.app.activeparks.ui.event.util

import android.content.Context
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.data.repository.Repository
import com.app.activeparks.data.storage.Preferences
import com.app.activeparks.ui.event.interfaces.ResponseCallBack
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class EventController(context: Context) {

    private val preferences: Preferences
    private val repository: Repository

    private val compositeDisposable = CompositeDisposable()

    init {
        preferences = Preferences(context)
        preferences.server = true
        repository = Repository(preferences)
    }


    fun setDataEvent(responseCallBack: ResponseCallBack, eventData: ItemEvent) {
        val dataSet = repository.setDataEvent(eventData).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({response ->
                responseCallBack.load(response.string())
            }) {}
        compositeDisposable.add(dataSet)
    }

    fun publishDataEvent(eventToken: String, responseCallBack: ResponseCallBack) {
        val publishData = repository.publishDataEvent(eventToken)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({responce ->
                responseCallBack.load(responce.string())
            }
            ) {}

        compositeDisposable.add(publishData)
    }

    fun deleteEvent(eventId: String) {

        val dataDeleted = repository.deleteEvent(eventId)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                clearDisposables()
            }
            ) {}

        compositeDisposable.add(dataDeleted)
    }

    fun getMyEvents(responseCallBack: ResponseCallBack) {

        val dataEvents = repository.myEvents
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                    responseFrom ->
                responseCallBack.load(responseFrom.string())
                clearDisposables()
            }
            ) {}

        compositeDisposable.add(dataEvents)
    }

    private fun clearDisposables() {
        compositeDisposable.clear()
    }

}