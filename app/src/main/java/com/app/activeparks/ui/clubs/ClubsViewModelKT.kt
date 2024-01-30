package com.app.activeparks.ui.clubs

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.activeparks.data.model.clubs.ClubsUserIsMemberModel
import com.app.activeparks.data.model.clubs.ItemClub
import com.app.activeparks.data.repository.Repository
import com.app.activeparks.data.storage.Preferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ClubsViewModelKT(
    private val repository: Repository,
    private val preferences: Preferences
) : ViewModel() {

    val clubList = MutableLiveData<List<ItemClub>>()

    @SuppressLint("CheckResult")
    fun getClubs() {
        if (preferences.userName.isNotEmpty()) {
            repository.myClubs.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result: ClubsUserIsMemberModel ->
                    val itemClubs: MutableList<ItemClub> = ArrayList()
                    for (item in result.items.userIsMember) {
                        item.isUser("userIsMember")
                        itemClubs.add(item)
                    }
                    for (item in result.items.userIsHead) {
                        item.isUser("userIsHead")
                        itemClubs.add(item)
                    }
                    clubList.setValue(itemClubs)
                }
        } else {
            clubList.value = listOf()
        }
    }
}