package com.app.activeparks.ui.people;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.model.Default;
import com.app.activeparks.data.model.clubs.ItemClub;
import com.app.activeparks.data.model.dictionaries.District;
import com.app.activeparks.data.model.dictionaries.Region;
import com.app.activeparks.data.model.sportevents.ItemEvent;
import com.app.activeparks.data.model.sportevents.SportEvents;
import com.app.activeparks.data.model.user.User;
import com.app.activeparks.data.model.uservideo.UserVideo;
import com.app.activeparks.data.model.workout.WorkoutItem;
import com.app.activeparks.data.storage.Preferences;
import com.app.activeparks.repository.Repository;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PeopleViewModel extends ViewModel {


    private final Preferences sharedPreferences;
    private Repository repository;

    public MutableLiveData<User> user = new MutableLiveData<>();
    public MutableLiveData<List<ItemClub>> clubsList = new MutableLiveData<>();
    public MutableLiveData<List<ItemEvent>> eventsList = new MutableLiveData<>();

    public MutableLiveData<SportEvents> resultList = new MutableLiveData<>();
    public MutableLiveData<List<WorkoutItem>> journalList = new MutableLiveData<>();
    public MutableLiveData<WorkoutItem> notes = new MutableLiveData<>();
    public MutableLiveData<List<WorkoutItem>> planList = new MutableLiveData<>();
    public MutableLiveData<UserVideo> userVideoList = new MutableLiveData<>();

    public MutableLiveData<String> message = new MutableLiveData<>();

    public MutableLiveData<Boolean> defaults = new MutableLiveData<>();


    private List<String> mRegion = new ArrayList<>();
    private List<String> mDictionaries = new ArrayList<>();
    private List<String> mDictionariesId = new ArrayList<>();
    private String districtId, regionId;
    public User mProfile;

    PeopleViewModel(Preferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        this.repository = new Repository(sharedPreferences);
    }

    public LiveData<List<ItemClub>> getClubs() {
        return clubsList;
    }

    public LiveData<List<ItemEvent>> getEvents() {
        return eventsList;
    }

    public LiveData<SportEvents> getResult() {
        return resultList;
    }

    public LiveData<UserVideo> getUserVideo() {
        return userVideoList;
    }

    public LiveData<WorkoutItem> getNotes() {
        return notes;
    }

    public LiveData<List<WorkoutItem>> getJournal() {
        return journalList;
    }

    public LiveData<List<WorkoutItem>> getPlan() {
        return planList;
    }


    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public LiveData<Boolean> getDefault() {
        return defaults;
    }

    public void user(String id) {
        repository.getUser(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> {
            user.setValue(result);
            mProfile = result;
        }, error -> {
        });
    }

    public void clubs(String id) {
        repository.getClubsUser(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()
        ).subscribe(result ->
        {
            List<ItemClub> itemClubs = new ArrayList<>();
            itemClubs.addAll(result.getItems().getUserIsMember());
            itemClubs.addAll(result.getItems().getUserIsHead());

            clubsList.setValue(itemClubs);
        }
                , error -> {
        });
    }

    public void clubs() {
        clubs(mProfile.getId());
    }



    public void result() {
        repository.getMyResult();
    }


    public void journal() {
        repository.journal(mProfile.getId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> journalList.setValue(result.getItems()), error -> Log.e("ProfileViewModel", "getPokemons: " + error.getMessage()));
    }

    public void notes(String id) {
        repository.workout(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> notes.setValue(result),
                        error -> Log.e("ProfileViewModel", "getPokemons: " + error.getMessage())
                );
    }

    public void plan() {
        repository.plans(mProfile.getId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> planList.setValue(result.getItems()),
                        error -> Log.e("ProfileViewModel", "getPokemons: " + error.getMessage())
                );
    }

    public void sendNotes(String id, String msg) {
        repository.addWorkoutNote(id, msg).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> notes(id),
                        error -> Log.e("ProfileViewModel", "getPokemons: " + error.getMessage())
                );
    }


    public List<String> getRegions() {
        mRegion.clear();
        for (Region region : sharedPreferences.getDictionarie().get(0).getRegions()) {
            mRegion.add(region.getTitle());
        }
        return mRegion;
    }

    public User userMapper(User user) {
        for (District district : sharedPreferences.getDictionarie().get(0).getDistricts()) {
            if (district.getId().equals(user.getDistrictId())) {
                user.setDistrictId(district.getTitle());
            }
        }
        for (Region region : sharedPreferences.getDictionarie().get(0).getRegions()) {
            if (region.getId().equals(user.getRegionId())) {
                user.setRegionId(region.getAlterTitle());
            }
        }
        return user;
    }
}