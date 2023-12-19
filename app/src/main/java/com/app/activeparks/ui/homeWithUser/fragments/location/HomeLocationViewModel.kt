package com.app.activeparks.ui.homeWithUser.fragments.location

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.activeparks.data.model.sportsgrounds.Sportsgrounds
import com.app.activeparks.data.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by O.Dziuba on 18.12.2023.
 */
class HomeLocationViewModel(
    private val repository: Repository
) : ViewModel() {

     val parksList = MutableLiveData<Sportsgrounds>()

    @SuppressLint("CheckResult")
    fun getParks() {
        repository.sportsgrounds(5, "30", "" + 30.51814, "" + 50.44812).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result: Sportsgrounds ->
                parksList.setValue(
                    result
                )
            }

    }
}