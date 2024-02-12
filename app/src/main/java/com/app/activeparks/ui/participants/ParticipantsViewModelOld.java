package com.app.activeparks.ui.participants;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.repository.Repository;
import com.app.activeparks.data.model.dictionaries.District;
import com.app.activeparks.data.model.user.User;
import com.app.activeparks.data.model.user.UserParticipants;
import com.app.activeparks.data.storage.Preferences;
import com.app.activeparks.ui.participants.util.ParticipantsTypes;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class ParticipantsViewModelOld extends ViewModel {

    private final Preferences sharedPreferences;
    private final Repository repository;
    private final MutableLiveData<UserParticipants> mUserClubsHeads = new MutableLiveData<>();
    private final MutableLiveData<UserParticipants> mUserClubsMembers = new MutableLiveData<>();
    private final MutableLiveData<UserParticipants> mUserApplying = new MutableLiveData<>();

    public boolean isEvent;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ParticipantsViewModelOld(Preferences sharedPreferences)   {
        this.sharedPreferences = sharedPreferences;
        this.repository = new Repository(sharedPreferences);
    }

    public void getClubsUser(String id){
        getClubsUser(id, true);
    }

    public void getClubsUser(String id, Boolean type){
        Disposable getClubsUser = repository.getClubsUser(id, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (type) {
                                mUserClubsHeads.setValue(userMapper(result));
                                getClubsUser(id, false);
                            }else {
                                mUserClubsMembers.setValue(userMapper(result));
                            }
                        },
                        error -> {
                        });

        compositeDisposable.add(getClubsUser);
    }

    public void getEventUser(String id, Boolean type){
        Disposable getEventUser = repository.getEventUser(id, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (type) {
                                mUserClubsHeads.setValue(userMapper(result));
                                getEventUser(id, false);
                            }else {
                                mUserClubsMembers.setValue(userMapper(result));
                            }
                        },
                        error -> {
                        });

        compositeDisposable.add(getEventUser);
    }

    public void getClubsUserApplying(String id){
        Disposable getClubsUserApplying = repository.getClubsUserApplying(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result != null){
                                mUserApplying.setValue(userMapper(result));
                            }
                        },
                        error -> {
                        });
        compositeDisposable.add(getClubsUserApplying);
    }

    public void getEventUserApplying(String id){
        Disposable getEventUserApplying = repository.getEventUserApplying(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result != null){
                                mUserApplying.setValue(userMapper(result));
                            }
                        },
                        error -> {
                        });
        compositeDisposable.add(getEventUserApplying);
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
        eventApplyingRequest(id, user, ParticipantsTypes.ACCEPT.getType());
    }

    public void rejectEventUser(String id, String user){
        eventApplyingRequest(id, user, ParticipantsTypes.REJECT.getType());
    }

    public void leavetEventUser(String id, String user){
        eventApplyingRequest(id, user, ParticipantsTypes.LEAVE.getType());
    }

    public void eventSetActing(String id, String user){
        eventApplyingRequest(id, user, ParticipantsTypes.SET_ACTION.getType());
    }

    public void eventRemoveActing(String id, String user){
        eventApplyingRequest(id, user, ParticipantsTypes.REMOVE_ACTION.getType());
    }

    ///CLUB
    public void acceptClubUser(String id, String user){
        clubsApplyingRequest(id, user, ParticipantsTypes.APPROVE_USER.getType());
    }

    public void rejectClubUser(String id, String user){
        clubsApplyingRequest(id, user, ParticipantsTypes.REJECT_USER.getType());
    }

    public void removeClubUser(String id, String user){
        clubsApplyingRequest(id, user, ParticipantsTypes.REMOVE_USER.getType());
    }

    public void clubSetActing(String id, String user){
        clubsApplyingRequest(id, user, ParticipantsTypes.SET_ACTION.getType());
    }

    public void clubRemoveActing(String id, String user){
        clubsApplyingRequest(id, user, ParticipantsTypes.REMOVE_ACTION.getType());
    }

    public void clubsApplyingRequest(String id, String user, String type){
        Disposable clubsApplyingRequest =  repository.clubsApplyingRequest(id, user, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> Log.d("log_request", "" + result.string()),
                        error -> Log.d("log_request", "" + error.getMessage()));
        compositeDisposable.add(clubsApplyingRequest);
    }

    public void eventApplyingRequest(String id, String user, String type){
        Disposable eventApplyingRequest = repository.eventApplyingRequest(id, user, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {},
                        error -> {
                        });
        compositeDisposable.add(eventApplyingRequest);
    }

    public UserParticipants userMapper(UserParticipants user){
        List<User> mUsertItem = new ArrayList<>();
        List<District> district = sharedPreferences.getDictionarie().getDistricts();
        for (User u : user.getItems()) {
            for (District d : district) {
                if (u.getDistrictId() != null && u.getDistrictId().equals(d.getId())) {
                    u.setDistrictId(d.getTitle());
                }
            }
            mUsertItem.add(u);
        }
        return new UserParticipants().setItems(mUsertItem);
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
