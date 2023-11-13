package com.app.activeparks.ui.participants;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.repository.Repository;
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
            removeClubUser(id, user);
        }
    }

    public void setActing(String id, String user){
        if (isEvent){
            eventSetActing(id, user);
        }else {
            clubSetActing(id, user);
        }
    }

    public void removeActing(String id, String user){
        if (isEvent){
            eventRemoveActing(id, user);
        }else {
            clubRemoveActing(id, user);
        }
    }

    ///EVENT
    public void acceptEventUser(String id, String user){
        eventApplyingRequest(id, user, "accept");
    }

    public void rejectEventUser(String id, String user){
        eventApplyingRequest(id, user, "reject");
    }

    public void leavetEventUser(String id, String user){
        eventApplyingRequest(id, user, "leave");
    }

    public void eventSetActing(String id, String user){
        eventApplyingRequest(id, user, "set-acting");
    }

    public void eventRemoveActing(String id, String user){
        eventApplyingRequest(id, user, "remove-acting");
    }

    ///CLUB
    public void acceptClubUser(String id, String user){
        clubsApplyingRequest(id, user, "approve-user");
    }

    public void rejectClubUser(String id, String user){
        clubsApplyingRequest(id, user, "reject-user");
    }

    public void removeClubUser(String id, String user){
        clubsApplyingRequest(id, user, "remove-user");
    }

    public void clubSetActing(String id, String user){
        clubsApplyingRequest(id, user, "set-acting");
    }

    public void clubRemoveActing(String id, String user){
        clubsApplyingRequest(id, user, "remove-acting");
    }

    public void clubsApplyingRequest(String id, String user, String type){
        repository.clubsApplyingRequest(id, user, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            Log.d("log_request", "" + result.string());},
                        error -> {Log.d("log_request", "" + error.getMessage());
                        });
    }

    public void eventApplyingRequest(String id, String user, String type){
        repository.eventApplyingRequest(id, user, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {},
                        error -> {
                        });
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
