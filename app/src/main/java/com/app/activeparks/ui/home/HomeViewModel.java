package com.app.activeparks.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.model.clubs.ItemClub;
import com.app.activeparks.data.model.sportevents.ItemEvent;
import com.app.activeparks.repository.Repository;
import com.app.activeparks.data.model.news.ItemNews;
import com.app.activeparks.data.model.news.News;
import com.app.activeparks.data.model.sportevents.SportEvents;
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
import io.reactivex.schedulers.Schedulers;


public class HomeViewModel extends ViewModel {

    private final Preferences preferences;
    private Repository repository;

    public MutableLiveData<User> user = new MutableLiveData<>();
    private MutableLiveData<Sportsgrounds> parksList = new MutableLiveData<>();
    private MutableLiveData<List<ItemEvent>> eventsList = new MutableLiveData<>();
    private MutableLiveData<News> newsList = new MutableLiveData<>();
    private MutableLiveData<ItemNews> newsDetails = new MutableLiveData<>();
    private MutableLiveData<List<ItemClub>> clubList = new MutableLiveData<>();
    private MutableLiveData<String> location = new MutableLiveData<>();

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
        repository.sportsgrounds(5, "30", "" + 30.51814, "" + 50.44812).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> parksList.setValue(result),
                        error -> {
                        });
    }

    public void getParks(double lat, double lon) {
        repository.sportsgrounds(5, "", "" + lon, "" + lat).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> parksList.setValue(result),
                        error -> {
                        });
    }

    public void getNews() {
        repository.news(5).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> newsList.setValue(result),
                        error -> Log.e("HomeViewModel", "getPokemons: " + error.getMessage()));
    }

    public void getEvents() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        Map<String, String> data = new HashMap<>();
        data.put("offset", "0");
        data.put("limit", "10");
        data.put("filters[startsFrom]", date);
        data.put("filters[startsTo]", date);
        data.put("sort[startsAt]", "asc");
        repository.eventsDay(data).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
    }

    public void clubs() {
        new Repository(preferences).getMyClubs().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
    }

    public void location(double lat, double lon) {
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
            user.setValue(preferences.getUser());
        }
    }

    public boolean getUserAuth() {
        if (preferences.getToken() != null && preferences.getToken().length() > 0) {
            return true;
        }
        return false;
    }
}