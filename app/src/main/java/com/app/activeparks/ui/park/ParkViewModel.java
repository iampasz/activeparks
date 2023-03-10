package com.app.activeparks.ui.park;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.repository.Repository;
import com.app.activeparks.data.model.sportsgrounds.ItemSportsground;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ParkViewModel extends ViewModel {

    private MutableLiveData<ItemSportsground> mItemEvent;
    private Repository repository;

    public ParkViewModel() {
        mItemEvent = new MutableLiveData<>();
        repository = new Repository();
    }

    public LiveData<ItemSportsground> getParkDetails() {
        return mItemEvent;
    }

    public void getPark(String id){
        repository.getSportsground(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> mItemEvent.setValue(result),
                        error -> {});
    }

}