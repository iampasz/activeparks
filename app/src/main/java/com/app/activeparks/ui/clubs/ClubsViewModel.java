package com.app.activeparks.ui.clubs;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.repository.Repository;
import com.app.activeparks.data.model.clubs.ItemClub;
import com.app.activeparks.data.storage.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class ClubsViewModel extends ViewModel {


    private final MutableLiveData<ItemClub> mClubsDetails;

    private final Repository repository;
    private final MutableLiveData<List<ItemClub>> clubsCreatorList = new MutableLiveData<>();
    private final MutableLiveData<List<ItemClub>> clubsParticipantList = new MutableLiveData<>();
    private final List<ItemClub> filter = new ArrayList<>();


    public Boolean admin = false, apply = false,  owner = false, member  = false;
    public String mId;


    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ClubsViewModel(Preferences sharedPreferences)   {
        repository = new Repository(sharedPreferences);
        mClubsDetails = new MutableLiveData<>();
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
        Disposable getMyClubs = repository.getMyClubs().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> clubsCreatorList.setValue(result.getItems().getUserIsHead()),
                        error -> {
                        }
                );

        compositeDisposable.add(getMyClubs);
    }

    public void getClubsParticipantList(){
        Disposable getMyClubs = repository.getMyClubs().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> clubsParticipantList.setValue(result.getItems().getUserIsMember()),
                        error -> {
                        }
                );

        compositeDisposable.add(getMyClubs);
    }

    public void getAllClubs(int limit){
        Disposable getClubsAll = repository.getClubsAll(String.valueOf(limit)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            filter.clear();
                            clubsParticipantList.setValue(result.getItems());
                            filter.addAll(result.getItems());
                        },
                        error -> {});

        compositeDisposable.add(getClubsAll);
    }

    public void getSearchClubs(String name){
        Disposable getSearchClubsAll = repository.getSearchClubsAll(name).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            filter.clear();
                            clubsParticipantList.setValue(result.getItems());
                            filter.addAll(result.getItems());
                        },
                        error -> Log.d("LOG","test" + error.getMessage()));
        compositeDisposable.add(getSearchClubsAll);
    }




    public void getClubsDetail(String id){
        this.mId = id;
        repository.getClubsDetails(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            mClubsDetails.setValue(result);
                            if (result.getClubUser() != null){
                                admin = (result.getClubUser().getIsCoordinator());
                                apply = true;
                            }else {
                                apply = false;
                            }
                        },
                        error -> {});
    }

    public void applyUser(){
        if (!apply) {
            Disposable applyUser = repository.applyUser(mId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> getClubsDetail(mId),
                            error -> {});
            compositeDisposable.add(applyUser);
        }else {
            if (Objects.requireNonNull(mClubsDetails.getValue()).getClubUser() != null) {
                if (mClubsDetails.getValue().getClubUser().getIsApproved()) {
                    removeUser();
                } else {
                    rejectUser();
                }
            }
        }
    }

    public void setPermissionsRequest(Boolean type){
        Disposable setPermissionsRequest = repository.setPermissionsRequest(mId, type).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {},
                        error -> {});

        compositeDisposable.add(setPermissionsRequest);
    }

    void filterData(String search) {
        if (search.length() > 2) {
            List<ItemClub> beerDrinkers = filter.stream()
                    .filter(p -> p.getName().toLowerCase().contains(search.toLowerCase())).collect(Collectors.toList());
            clubsParticipantList.setValue(beerDrinkers);
        }else{
            clubsParticipantList.setValue(filter);
        }
    }


    public void rejectUser(){
        Disposable rejectUser =  repository.rejectUser(mId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> getClubsDetail(mId),
                        error -> {});

        compositeDisposable.add(rejectUser);
    }

    public void removeUser(){
        Disposable removeUser = repository.removeUser(mId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> getClubsDetail(mId),
                        error -> {});

        compositeDisposable.add(removeUser);
    }
}