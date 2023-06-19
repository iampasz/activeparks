package com.app.activeparks.ui.news;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.repository.Repository;
import com.app.activeparks.data.model.news.ItemNews;
import com.app.activeparks.data.model.news.News;
import com.app.activeparks.data.model.news.NewsClubs;
import com.app.activeparks.data.storage.Preferences;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class NewsViewModel extends ViewModel {

    private final Preferences sharedPreferences;

    private MutableLiveData<News> newsList = new MutableLiveData<>();
    private MutableLiveData<ItemNews> newsDetails = new MutableLiveData<>();
    private Repository repository;

    public NewsViewModel(Preferences sharedPreferences)   {
        this.sharedPreferences = sharedPreferences;
        this.repository = new Repository(sharedPreferences);
    }

    public LiveData<News> getNewsList() {
        return newsList;
    }

    public LiveData<ItemNews> getNewsDetails() {
        return newsDetails;
    }

    public LiveData<ItemNews> getClubNewsDetails() {
        return newsDetails;
    }

    public void getNews(){
        repository.news(100).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> newsList.setValue(result),
                        error -> {});
    }

    public void getNewsId(String id){
        repository.getClubsNews(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> newsList.setValue(result),
                        error -> {});
    }

    public void getNewsDetails(String id){
        repository.newsDetails(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> newsDetails.setValue(result),
                        error -> {});
    }

    public void getNewsDetails(String club, String id){
        repository.getNewsClubDetails(club, id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> newsDetails.setValue(result),
                        error -> {});
    }


    public void setClubsCreatorNews(String clubId, String title, String body){

        NewsClubs news = new NewsClubs();
        news.setId(sharedPreferences.getId());
        news.setClubId(clubId);
        news.setClubId(title);
        news.setClubId(body);
        repository.setClubsNews(clubId, news);
    }

    public void updateFile(File file){
        repository.updateFile( file, "other_photo");
    }
}