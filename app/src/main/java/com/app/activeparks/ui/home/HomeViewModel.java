package com.app.activeparks.ui.home;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.model.clubs.ItemClub;
import com.app.activeparks.data.model.news.ItemNews;
import com.app.activeparks.data.model.sportevents.ItemEvent;
import com.app.activeparks.data.repository.Repository;
import com.app.activeparks.data.model.news.News;
import com.app.activeparks.data.model.sportsgrounds.Sportsgrounds;
import com.app.activeparks.data.model.user.User;
import com.app.activeparks.data.storage.Preferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class HomeViewModel extends ViewModel {

    private final Preferences preferences;
    private final Repository repository;

    public MutableLiveData<User> user = new MutableLiveData<>();
    private final MutableLiveData<Sportsgrounds> parksList = new MutableLiveData<>();
    private final MutableLiveData<List<ItemEvent>> eventsList = new MutableLiveData<>();
    private final MutableLiveData<News> newsList = new MutableLiveData<>();
    private final MutableLiveData<ItemNews> newsDetails = new MutableLiveData<>();
    private final MutableLiveData<List<ItemClub>> clubList = new MutableLiveData<>();
    private final MutableLiveData<String> location = new MutableLiveData<>();

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    public HomeViewModel(Preferences sharedPreferences) {
        preferences = sharedPreferences;
        repository = new Repository(sharedPreferences);
    }

    public void setInitialData() {
        getDataUser();
        getParks();
        getNews();
        getEvents();
    }

    public LiveData<List<ItemEvent>> getSportEventsList() {
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

    public void getParks() {
        Disposable sportsGroundsRequest = repository.sportsgrounds(5, "30", "" + 30.51814, "" + 50.44812).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(parksList::setValue,
                        error -> {
                        });
        compositeDisposable.add(sportsGroundsRequest);
    }

//    public void getParks(double lat, double lon) {
//        Disposable sportsGroundsRequest = repository.sportsgrounds(5, "", "" + lon, "" + lat).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(parksList::setValue,
//                        error -> {
//                        });
//    }

    public void getNews() {
        Disposable newsRequest = repository.news(5).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsList::setValue,
                        error -> Log.e("HomeViewModel", "getPokemons: " + error.getMessage()));
        compositeDisposable.add(newsRequest);
    }

    public void getEvents() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        Map<String, String> data = new HashMap<>();
        data.put("offset", "0");
        data.put("limit", "10");
        data.put("filters[startsFrom]", date);
        data.put("filters[startsTo]", date);
        data.put("sort[startsAt]", "asc");
        Disposable eventsDayRequest = repository.eventsDay(data).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            List<ItemEvent> filteredList = new ArrayList<>();
                            for (ItemEvent element : result.getItems()) {
                                if (!element.getHoldingStatusId().equals("0q8a6xc0-1nb4-1pr4-h5at-4sw3m0l387yp")) {
                                    filteredList.add(element);
                                }
                            }
                            eventsList.setValue(filteredList);
                            },
                        error -> {
                        });
        compositeDisposable.add(eventsDayRequest);
    }

    public void clubs() {
        Disposable getMyClubsRequest = new Repository(preferences).getMyClubs().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            List<ItemClub> itemClubs = new ArrayList<>();
                            for (ItemClub item: result.getItems().getUserIsMember()){
                                item.isUser("userIsMember");
                                itemClubs.add(item);
                            }
                            for (ItemClub item: result.getItems().getUserIsHead()){
                                item.isUser("userIsHead");
                                itemClubs.add(item);
                            }

                            clubList.setValue(itemClubs);},
                        error -> {
                        }
                );
        compositeDisposable.add(getMyClubsRequest);
    }

    public void location(double lat, double lon) {
        Disposable locationRequest = repository.location(String.valueOf(lat), String.valueOf(lon)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> location.setValue(result.getAddress().getCity()),
                        error -> location.setValue("Київ"));
        compositeDisposable.add(locationRequest);
    }

    private void getDataUser() {
        if (preferences.getUser() != null) {
            user.setValue(preferences.getUser());
        }
    }

    public boolean getUserAuth() {
        return preferences.getToken() != null && preferences.getToken().length() > 0;
    }
}