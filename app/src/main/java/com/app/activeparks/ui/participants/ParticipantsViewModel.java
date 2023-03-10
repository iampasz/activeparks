package com.app.activeparks.ui.participants;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.repository.Repository;
import com.app.activeparks.data.model.dictionaries.District;
import com.app.activeparks.data.model.dictionaries.Region;
import com.app.activeparks.data.model.user.User;
import com.app.activeparks.data.model.user.UserClubs;
import com.app.activeparks.data.storage.Preferences;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ParticipantsViewModel extends ViewModel {

    private final Preferences sharedPreferences;
    private Repository repository;
    private MutableLiveData<UserClubs> mUserClubsHeads = new MutableLiveData<>();
    private MutableLiveData<UserClubs> mUserClubsMembers = new MutableLiveData<>();
    private MutableLiveData<UserClubs> mUserClubsApplying = new MutableLiveData<>();

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
                                mUserClubsApplying.setValue(userMapper(result));
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

    public void rejectUser(String id, String user){
//        new repository().rejectUser(sharedPreferences.getToken(), id, user).setOnListener(new repository.ResponseListener<Response<ResponseBody>>() {
//            @Override
//            public void onResponse(Response<ResponseBody> user) {
//                if (user != null){
//                }
//            }
//        });
    }


    public UserClubs userMapper(UserClubs user){
        List<User> mUsertItem = new ArrayList<>();
        List<District> district = sharedPreferences.getDictionarie().get(0).getDistricts();
        List<Region> region = sharedPreferences.getDictionarie().get(0).getRegions();
        for (User u : user.getItems()) {
            for (District d : district) {
                if (u.getDistrictId() != null && u.getDistrictId().equals(d.getId())) {
                    u.setDistrictId(d.getTitle());
                }
            }
            mUsertItem.add(u);
        }
        UserClubs userClubs = new UserClubs().setItems(mUsertItem);
        return userClubs;
    }

    public LiveData<UserClubs> getUserHeads() {
        return mUserClubsHeads;
    }

    public LiveData<UserClubs> getUserMembers() {
        return mUserClubsMembers;
    }

    public LiveData<UserClubs> getUserApplying() {
        return mUserClubsApplying;
    }
}
