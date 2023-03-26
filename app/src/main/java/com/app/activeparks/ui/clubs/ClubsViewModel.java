package com.app.activeparks.ui.clubs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.repository.Repository;
import com.app.activeparks.data.model.clubs.Clubs;
import com.app.activeparks.data.model.clubs.ItemClub;
import com.app.activeparks.data.storage.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ClubsViewModel extends ViewModel {

    private final Preferences sharedPreferences;
    private Repository repository;
    private MutableLiveData<List<ItemClub>> clubsCreatorList = new MutableLiveData<>();
    private MutableLiveData<List<ItemClub>> clubsParticipantList = new MutableLiveData<>();
    private List<ItemClub> filter = new ArrayList<>();
    private MutableLiveData<ItemClub> mClubsDetails = new MutableLiveData<>();
    public Boolean admin = false, apply = false, userInClub = false,  owner = false, member  = false;
    public String mId;

    public ClubsViewModel(Preferences sharedPreferences)   {
        this.sharedPreferences = sharedPreferences;
        repository = new Repository(sharedPreferences);
    }

    public LiveData<List<ItemClub>> getClubsCreator() {
        return clubsCreatorList;
    }

    public LiveData<List<ItemClub>> getClubsParticipant() {
        return clubsParticipantList;
    }

    public LiveData<ItemClub> getEventDetails() {
        return mClubsDetails;
    }


    public void getClubsCreatorList(){
        repository.getClubsCreator();
    }

    private void getClubsParticipantList(){
        repository.getClubsParticipant().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            clubsParticipantList.setValue(result.getItems());
                        },
                        error -> {});
    }

    public void getAllClubs(){
        repository.getClubsAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            clubsParticipantList.setValue(result.getItems());
                            filter.addAll(result.getItems());
                        },
                        error -> {});
    }

    public void getClubsDetail(String id){
        this.mId = id;
        repository.getClubsDetails(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            mClubsDetails.setValue(result);
                            if (result.getClubUser() != null){
                                admin = result.getClubUser().getIsCoordinator();
                                apply = true;
                            }else {
                                apply = false;
                            }
                        },
                        error -> {});
    }

    public void applyUser(){
        if (apply == false) {
            repository.applyUser(mId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> getClubsDetail(mId),
                            error -> {});
        }else {
            if (userInClub == true) {
                removeUser();
            } else {
                rejectUser();
            }
        }
    }

    void filterData(String search) {
        if (search.length() > 2) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<ItemClub> beerDrinkers = filter.stream()
                        .filter(p -> p.getName().toLowerCase().contains(search.toLowerCase())).collect(Collectors.toList());
                clubsParticipantList.setValue(beerDrinkers);
            }
        }else{
            clubsParticipantList.setValue(filter);
        }
    }


    public void rejectUser(){
        repository.rejectUser(mId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> getClubsDetail(mId),
                        error -> {});
    }

    public void removeUser(){
        repository.removeUser(mId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> getClubsDetail(mId),
                        error -> {});
    }
}