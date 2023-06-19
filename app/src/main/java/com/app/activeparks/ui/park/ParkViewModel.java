package com.app.activeparks.ui.park;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.model.dictionaries.BaseDictionaries;
import com.app.activeparks.data.storage.Preferences;
import com.app.activeparks.repository.Repository;
import com.app.activeparks.data.model.sportsgrounds.ItemSportsground;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ParkViewModel extends ViewModel {

    private Preferences preferences;
    private Repository repository;
    private MutableLiveData<ItemSportsground> mItemEvent;

    public ParkViewModel(Preferences sharedPreferences) {
        preferences = sharedPreferences;
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

    public String capacity(String status) {
        if (preferences.getDictionarie() != null) {
            for (BaseDictionaries eventHoldingStatuses : preferences.getDictionarie().getSportsgroundCapacities()) {
                if (status.equals(eventHoldingStatuses.getId())) {
                    return eventHoldingStatuses.getTitle();
                }
            }

        }
        return "Немає даних";
    }

    public String accessTypeId(String status) {
        if (preferences.getDictionarie() != null) {
            for (BaseDictionaries eventHoldingStatuses : preferences.getDictionarie().getSportsgroundAccessTypes()) {
                if (status.equals(eventHoldingStatuses.getId())) {
                    return eventHoldingStatuses.getTitle();
                }
            }

        }
        return "Немає даних";
    }

    public String sportsgroundType(String status) {
        if (preferences.getDictionarie() != null) {
            for (BaseDictionaries eventHoldingStatuses : preferences.getDictionarie().getSportsgroundTypes()) {
                if (status.equals(eventHoldingStatuses.getId())) {
                    return eventHoldingStatuses.getTitle();
                }
            }

        }
        return "Немає даних";
    }

    public String ownershipType(String status) {
        if (preferences.getDictionarie() != null) {
            for (BaseDictionaries eventHoldingStatuses : preferences.getDictionarie().getOwnershipTypes()) {
                if (status.equals(eventHoldingStatuses.getId())) {
                    return eventHoldingStatuses.getTitle();
                }
            }

        }
        return "Немає даних";
    }
}