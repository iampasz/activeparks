package com.app.activeparks.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.model.clubs.ItemClub;
import com.app.activeparks.repository.Repository;
import com.app.activeparks.data.model.news.ItemNews;
import com.app.activeparks.data.model.news.News;
import com.app.activeparks.data.model.sportevents.SportEvents;
import com.app.activeparks.data.model.sportsgrounds.Sportsgrounds;
import com.app.activeparks.data.model.user.User;
import com.app.activeparks.data.storage.Preferences;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class HomeViewModel extends ViewModel {

    private final Preferences preferences;
    private Repository repository;

    public MutableLiveData<User> user = new MutableLiveData<>();
    private MutableLiveData<Sportsgrounds> parksList = new MutableLiveData<>();
    private MutableLiveData<SportEvents> eventsList = new MutableLiveData<>();
    private MutableLiveData<News> newsList = new MutableLiveData<>();
    private MutableLiveData<ItemNews> newsDetails = new MutableLiveData<>();
    private MutableLiveData<List<ItemClub>> clubList = new MutableLiveData<>();
    private MutableLiveData<String> location = new MutableLiveData<>();

    public HomeViewModel(Preferences sharedPreferences) {
        preferences = sharedPreferences;
        repository = new Repository(sharedPreferences);
    }

    public void setInitialData(){
        getDataUser();
        getParks();
        getNews();
        getSportEvents();
    }

    public LiveData<SportEvents> getSportEventsList() {
        return eventsList;
    }

    public LiveData<News> getNewsList() {
        return newsList;
    }

    public LiveData<ItemNews> getNewsDetails() {
        return newsDetails;
    }

    public LiveData<Sportsgrounds> getParksList() {
        return parksList;
    }

    public LiveData<List<ItemClub>> getClubs() {
        return clubList;
    }

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<String> getLocation() {
        return location;
    }

    public void getParks(){
        repository.sportsgrounds(5,"30","" + 30.37593, "" + 50.46710).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> parksList.setValue(result),
                        error -> {});
    }

    public void getParks(double lat, double lon){
        repository.sportsgrounds(5,"30","" + lon, "" + lat).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> parksList.setValue(result),
                        error -> {});
    }

    public void getNews(){
        repository.news(5).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> newsList.setValue(result),
                        error ->  Log.e("HomeViewModel", "getPokemons: " + error.getMessage()));
    }

    public void getSportEvents(){
        repository.events(5).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> eventsList.setValue(result),
                        error -> {});
    }

    public void clubs() {
        repository.getMyClubs().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> clubList.setValue(result.getItems().getUserIsMember()),
                        error -> {}
                );
    }

    public void location(double lat, double lon){
        repository.location(String.valueOf(lat), String.valueOf(lon)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            location.setValue("місцеположення визначено по геолокації - " + result.getAddress().getCity());
                        },
                        error -> {
                            location.setValue("Київ");
                        });
    }

    private void getDataUser() {
        if (preferences.getUser() != null) {
            user.setValue(preferences.getUser().get(0));
        }
    }

    public boolean getUserAuth() {
        return preferences.getToken().length() > 1 ? true : false;
    }
}