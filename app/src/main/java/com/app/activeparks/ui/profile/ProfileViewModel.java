package com.app.activeparks.ui.profile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.model.Default;
import com.app.activeparks.data.model.clubs.Clubs;
import com.app.activeparks.data.model.sportevents.SportEvents;
import com.app.activeparks.data.model.workout.WorkoutModel;
import com.app.activeparks.repository.Repository;
import com.app.activeparks.data.model.clubs.ItemClub;
import com.app.activeparks.data.model.dictionaries.District;
import com.app.activeparks.data.model.dictionaries.Region;
import com.app.activeparks.data.model.sportevents.ItemEvent;
import com.app.activeparks.data.model.user.User;
import com.app.activeparks.data.model.uservideo.UserVideo;
import com.app.activeparks.data.model.workout.WorkoutItem;
import com.app.activeparks.data.storage.Preferences;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProfileViewModel extends ViewModel {


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

    ProfileViewModel(Preferences sharedPreferences) {
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

    public void user() {
        if (sharedPreferences.getUser() != null) {
            user.setValue(userMapper(sharedPreferences.getUser().get(0)));
        }
        repository.getUser().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> {
            user.setValue(userMapper(result));
            mProfile = result;
            List<User> users = new ArrayList<>();
            users.add(result);
            sharedPreferences.setUser(users);
        }, error -> {
            if (sharedPreferences.getUser() != null) {
                user.setValue(userMapper(sharedPreferences.getUser().get(0)));
            }
            if (error.getMessage().contains("401")) {
                logout();
            }
        });
    }

    public void clubs() {
        repository.getMyClubs().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> clubsList.setValue(result.getItems().getUserIsMember()), error -> {
        });
    }

    public void event() {
        repository.myEvents().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> eventsList.setValue(result.getItems()), error -> {
        });
    }

    public void result() {
        repository.getMyResult();
    }

    public void userVideoList() {
        repository.getUserVideos().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> userVideoList.setValue(result), error -> {
        });
    }

    public void journal() {
        repository.journal().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> journalList.setValue(result.getItems()), error -> Log.e("ProfileViewModel", "getPokemons: " + error.getMessage()));
    }

    public void notes(String id) {
        repository.workout(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> notes.setValue(result),
                        error -> Log.e("ProfileViewModel", "getPokemons: " + error.getMessage())
                );
    }

    public void plan() {
        repository.plans().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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

    public void cangeNotes(String id, String idNotes, String msg) {
        repository.cangeNotes(id, idNotes, msg).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> notes(id),
                        error -> Log.e("ProfileViewModel", "getPokemons: " + error.getMessage())
                );
    }

    public void updateUser() {
        repository.updateUser(mProfile).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            message.setValue("Дані оновлені!");
                            defaults.setValue(true);
                        },
                        error -> {
                            try {
                                Default def = new Gson().fromJson(error.getMessage(), Default.class);
                                if (def.getErrors() != null) {
                                    message.setValue(def.getErrors().get(0).getMsg());
                                }
                            }catch (Exception e){
                                message.setValue("Перевірте підключення до інтернету");
                            }
                        }
                );
    }

    public void updateFile(File file) {
        repository.updateFile(file, "avatar").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result.getUrl() != null){
                                mProfile.setPhoto(result.getUrl());
                                updateUser();
                            }
                        },
                        error -> {
                            message.setValue("Перевірте підключення до інтернету");
                        }
                );
    }

    public void removeUser() {
        repository.removeUser();
        sharedPreferences.setToken("");
        sharedPreferences.setId("");
        sharedPreferences.clear();
    }


    public List<String> getRegions() {
        mRegion.clear();
        for (Region region : sharedPreferences.getDictionarie().get(0).getRegions()) {
            mRegion.add(region.getTitle());
        }
        return mRegion;
    }

    public String getRegionId(int id) {
        return sharedPreferences.getDictionarie().get(0).getRegions().get(id).getId();
    }

    public List<String> getDictionarie(int index) {
        mDictionaries.clear();
        String getRegionId = sharedPreferences.getDictionarie().get(0).getRegions().get(index).getId();
        for (District district : sharedPreferences.getDictionarie().get(0).getDistricts()) {
            if (district.getRegionId().equals(getRegionId)) {
                mDictionaries.add(district.getTitle());
                mDictionariesId.add(district.getId());
            }
        }
        return mDictionaries;
    }

    public String getDictionarieId(int id) {
        return mDictionariesId.get(id);
    }

    public int getRegionsIndex() {
        int i = 0;
        for (String region : mRegion) {
            if (region.equals(districtId)) {
                i++;
                return i;
            }
        }
        return 0;
    }

    public int getDictionarieIndex() {
        int i = 0;
        for (String district : mDictionaries) {
            if (district.equals(regionId)) {
                i++;
            }
        }
        return i;
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

    public Boolean isProfile(String roleId) {
        return roleId.contains("62fe0318-c64a-490c-859d-9d313eacbf41") || roleId.contains("a2c99acd-0014-4fb3-8274-ad6a842f50ac1");
    }

    public void logout() {
        repository.logout();
        sharedPreferences.setToken(null);
        sharedPreferences.setId(null);
        sharedPreferences.clear();
    }
}