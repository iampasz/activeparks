package com.app.activeparks.ui.event.util

import android.content.Context
import android.util.Log
import com.app.activeparks.data.model.Default
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.data.repository.Repository
import com.app.activeparks.data.storage.Preferences
import com.app.activeparks.ui.event.interfaces.responseSuccessful
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.File

class EventController(context: Context) {

    private val preferences: Preferences
    private val repository: Repository

    private val compositeDisposable = CompositeDisposable()

    init {
        preferences = Preferences(context)
        preferences.server = true
        repository = Repository(preferences)
    }

    fun loadFileToAPI(file: File, eventData: ItemEvent) {

        val loadedFile = repository.updateFile(file, "other_photo")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result: Default ->
                    result.url?.let {
                        eventData.imageUrl = result.url
                    }
                }
            ) {
            }

        compositeDisposable.add(loadedFile)
    }

    fun setDataEvent(loaded: responseSuccessful, eventData: ItemEvent) {
        val dataSet = repository.setDataEvent(eventData).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                loaded.load()
            }) { throwable ->
                Log.e("ERROR", "Error: ${throwable.message}")
            }

        compositeDisposable.add(dataSet)
    }

    fun publishDataEvent(eventToken: String, responseSuccessful: responseSuccessful) {
        val publishData = repository.publishDataEvent(eventToken)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                responseSuccessful.load()
            }
            ) { throwable -> Log.i("API SERVICE", throwable.message + " NOU") }

        compositeDisposable.add(publishData)
    }


    fun deleteEvent(eventId: String) {

        val dataDeleted = repository.deleteEvent(eventId)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.i("API_SERVICE", "Data was delete from API")
                clearDisposables()
            }
            ) { Log.i("API_SERVICE", "Data wasn't delete from API") }

        compositeDisposable.add(dataDeleted)
    }

    fun clearDisposables() {
        compositeDisposable.clear()
    }

}