package com.app.activeparks.ui.profile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.model.Default;
import com.app.activeparks.data.model.dictionaries.BaseDictionaries;
import com.app.activeparks.data.model.sportevents.SportEvents;
import com.app.activeparks.data.model.user.UserUpdate;
import com.app.activeparks.data.repository.Repository;
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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProfileViewModel extends ViewModel {


    public final Preferences sharedPreferences;
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

    public int select = 0;
    private String districtId, regionId;
    public User mProfile;

    public UserUpdate userUpdate = new UserUpdate();

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
            User users = sharedPreferences.getUser();
            user.setValue(users);
            mProfile = users;
        }
        repository.getUser().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> {
            user.setValue(result);
            mProfile = result;
            sharedPreferences.setUser(result);
        }, error -> {
            if (sharedPreferences.getUser() != null) {
                user.setValue(userMapper(sharedPreferences.getUser()));
            }
            if (error.getMessage().contains("401")) {
                logout();
            }
        });
    }

    public void clubs() {
        select = 0;
        repository.getMyClubs().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            List<ItemClub> itemClubs = new ArrayList<>();
                            for (ItemClub item: result.getItems().getUserIsMember()){
                                item.isUser("userIsMember");
                                itemClubs.add(item);
                            }
                            for (ItemClub item: result.getItems().getUserIsHead()){
                                item.isUser("userIsHead");
                                itemClubs.add(item);
                            }

                            clubsList.setValue(itemClubs);
                        },
                        error -> {
                        });
    }

    public void event() {
        select = 1;
        repository.myevents().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            try {
                                List<ItemEvent> events = new ArrayList<>();
                                for (ItemEvent items : result.getItems()) {
                                    if (items.getHoldingStatusId() != null) {
                                        items.setHoldingStatusText(statusMapper(items.getHoldingStatusId()));
                                        events.add(items);
                                    }
                                }
                                eventsList.setValue(events);
                            } catch (Exception e) {

                            }
                        },
                        error -> {
                        });
    }

    public void result() {
        repository.getMyResult().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> {
        }, error -> {
        });
    }

    public void userVideoList() {
        select = 3;
        repository.getUserVideos().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> userVideoList.setValue(result), error -> {
        });
    }

    public void journal() {
        select = 2;
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
                        error -> planList.setValue(null)
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
        userUpdate.setId(mProfile.getId());
        userUpdate.setId(mProfile.getNickname());
        repository.updateUser(userUpdate).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            message.setValue("Дані оновлені!");
                            defaults.setValue(true);
                        },
                        error -> {
                            try {
                                Log.d("test_log", error.getMessage());
                                Default def = new Gson().fromJson(error.getMessage(), Default.class);
                                if (def.getErrors() != null) {
                                    message.setValue(def.getErrors().get(0).getMsg());
                                }
                            } catch (Exception e) {
                                Log.d("test_log", "msg" + e.getMessage());
                                message.setValue("Перевірте підключення до інтернету");
                            }
                        }
                );
    }

    public void updateFile(File file) {
        repository.updateFile(file, "avatar").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result.getUrl() != null) {
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
        repository.removeUser().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> {
        }, error -> {
        });
        sharedPreferences.clear();
    }


    public List<String> getRegions() {
        mRegion.clear();
        for (Region region : sharedPreferences.getDictionarie().getRegions()) {
            mRegion.add(region.getTitle());
        }
        return mRegion;
    }

    public String getRegionId(int id) {
        return sharedPreferences.getDictionarie().getRegions().get(id).getId();
    }

    public List<String> getDictionarie(int index) {
        mDictionaries.clear();
        String getRegionId = sharedPreferences.getDictionarie().getRegions().get(index).getId();
        for (District district : sharedPreferences.getDictionarie().getDistricts()) {
            if (district.getRegionId().equals(getRegionId)) {
                mDictionaries.add(district.getTitle());
                mDictionariesId.add(district.getId());
            }
        }
        return mDictionaries;
    }

    public String statusMapper(String status) {
        if (sharedPreferences.getDictionarie() != null) {
            for (BaseDictionaries eventHoldingStatuses : sharedPreferences.getDictionarie().getEventHoldingStatuses()) {
                if (status.equals(eventHoldingStatuses.getId())) {
                    return eventHoldingStatuses.getTitle();
                }
            }

        }
        return "не відомо";
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
        if (sharedPreferences.getDictionarie() != null) {
            for (District district : sharedPreferences.getDictionarie().getDistricts()) {
                if (district.getId().equals(user.getDistrictId())) {
                    user.setDistrictId(district.getTitle());
                }
            }
            for (Region region : sharedPreferences.getDictionarie().getRegions()) {
                if (region.getId().equals(user.getRegionId())) {
                    user.setRegionId(region.getAlterTitle());
                }
            }
        }
        return user;
    }

    public String isRole(String role) {
        if (sharedPreferences.getDictionarie() != null) {
            for (BaseDictionaries baseDictionaries : sharedPreferences.getDictionarie().getUserRoles()) {
                if (baseDictionaries.getId().equals(role)) {
                    return baseDictionaries.getTitle();
                }
            }
        }
        return "Користувач";
    }

    public Boolean isProfile(String roleId) {
        return roleId.contains("631db81f-fa07-42e2-b394-062c9be66b09") || roleId.contains("09efbeb2-f45a-418d-89b0-b2a4c37f6122") || roleId.contains("5dcf0363-d171-45db-9280-cb337ca5e101") || roleId.contains("d379ecaa-fee7-48a4-84df-a176f47820e6");
    }

    public void logout() {
        repository.logout();
        sharedPreferences.setToken(null);
        sharedPreferences.setId(null);
        sharedPreferences.clear();
    }

    public void setServer(Boolean type) {
        sharedPreferences.setServer(type);
    }

    public Boolean getServer() {
        return sharedPreferences.getServer();
    }
}