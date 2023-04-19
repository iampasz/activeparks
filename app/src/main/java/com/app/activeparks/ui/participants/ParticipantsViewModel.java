package com.app.activeparks.ui.participants;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.repository.Repository;
import com.app.activeparks.data.model.dictionaries.District;
import com.app.activeparks.data.model.dictionaries.Region;
import com.app.activeparks.data.model.user.User;
import com.app.activeparks.data.model.user.UserParticipants;
import com.app.activeparks.data.storage.Preferences;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ParticipantsViewModel extends ViewModel {

    private final Preferences sharedPreferences;
    private Repository repository;
    private MutableLiveData<UserParticipants> mUserClubsHeads = new MutableLiveData<>();
    private MutableLiveData<UserParticipants> mUserClubsMembers = new MutableLiveData<>();
    private MutableLiveData<UserParticipants> mUserApplying = new MutableLiveData<>();

    public boolean isEvent;


    public ParticipantsViewModel(Preferences sharedPreferences)   {
        this.sharedPreferences = sharedPreferences;
        this.repository = new Repository(sharedPreferences);
    }

    public void getClubsUser(String id){
        getClubsUser(id, true);
    }

    public void getClubsUser(String id, Boolean type){
        repository.getClubsUser(id, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (type == true) {
                                mUserClubsHeads.setValue(userMapper(result));
                                getClubsUser(id, false);
                            }else {
                                mUserClubsMembers.setValue(userMapper(result));
                            }
                        },
                        error -> {
                        });
    }

    public void getEventUser(String id, Boolean type){
        repository.getEventUser(id, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (type == true) {
                                mUserClubsHeads.setValue(userMapper(result));
                                getEventUser(id, false);
                            }else {
                                mUserClubsMembers.setValue(userMapper(result));
                            }
                        },
                        error -> {
                        });
    }

    public void getClubsUserApplying(String id){
        repository.getClubsUserApplying(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result != null){
                                mUserApplying.setValue(userMapper(result));
                            }
                        },
                        error -> {
                        });
    }

    public void getEventUserApplying(String id){
        repository.getEventUserApplying(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result != null){
                                mUserApplying.setValue(userMapper(result));
                            }
                        },
                        error -> {
                        });
    }

    public void approveUser(String id, String user){
        repository.approveUser(id, user).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {},
                        error -> {
                        });
    }


    public void leaveUser(String id, String user){
        repository.rejectUser(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {},
                        error -> {
                        });
    }

    public void acceptEventUser(String id, String user){
        repository.eventApplyingRequest(id, user, "accept").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {},
                        error -> {
                        });
    }

    public void rejectEventUser(String id, String user){
        repository.eventApplyingRequest(id, user, "reject").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {},
                        error -> {
                        });
    }

    public void leavetEventUser(String id, String user){
        repository.eventApplyingRequest(id, user, "leave").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {},
                        error -> {
                        });
    }

    public void acceptClubUser(String id, String user){
        repository.clubsApplyingRequest(id, user, "approve-user").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {},
                        error -> {
                        });
    }

    public void rejectClubUser(String id, String user){
        repository.clubsApplyingRequest(id, user, "reject-user").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {},
                        error -> {
                        });
    }

    public void leavetClubUser(String id, String user){
        repository.clubsApplyingRequest(id, user, "remove-user").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {},
                        error -> {
                        });
    }

    public void acceptUser(String id, String user){
        if (isEvent){
            acceptEventUser(id, user);
        }else {
            acceptClubUser(id, user);
        }
    }

    public void rejectUser(String id, String user){
        if (isEvent){
            rejectEventUser(id, user);
        }else {
            rejectClubUser(id, user);
        }
    }

    public void removeUser(String id, String user){
        if (isEvent){
            leavetEventUser(id, user);
        }else {
            leavetClubUser(id, user);
        }
    }


    public UserParticipants userMapper(UserParticipants user){
        List<User> mUsertItem = new ArrayList<>();
        List<District> district = sharedPreferences.getDictionarie().getDistricts();
        List<Region> region = sharedPreferences.getDictionarie().getRegions();
        for (User u : user.getItems()) {
            for (District d : district) {
                if (u.getDistrictId() != null && u.getDistrictId().equals(d.getId())) {
                    u.setDistrictId(d.getTitle());
                }
            }
            mUsertItem.add(u);
        }
        UserParticipants userClubs = new UserParticipants().setItems(mUsertItem);
        return userClubs;
    }

    public LiveData<UserParticipants> getUserHeads() {
        return mUserClubsHeads;
    }

    public LiveData<UserParticipants> getUserMembers() {
        return mUserClubsMembers;
    }

    public LiveData<UserParticipants> getUserApplying() {
        return mUserApplying;
    }
    public void setIsEvent(boolean isEvent) {
        this.isEvent = isEvent;
    }
}
