package com.app.activeparks.ui.news;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.model.news.ItemNews;
import com.app.activeparks.data.repository.Repository;
import com.app.activeparks.data.model.news.News;
import com.app.activeparks.data.model.news.NewsClubs;
import com.app.activeparks.data.storage.Preferences;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class NewsViewModel extends ViewModel {

    private final Preferences sharedPreferences;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<News> newsList = new MutableLiveData<>();
    private final MutableLiveData<ItemNews> newsDetails = new MutableLiveData<>();
    private final Repository repository;

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

//    public LiveData<ItemNews> getClubNewsDetails() {
//        return newsDetails;
//    }

    public void getNews(){
        Disposable newsRequest = repository.news(100).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsList::setValue,
                        error -> {});

        compositeDisposable.add(newsRequest);
    }

    public void getNewsId(String id){
        Disposable getClubsNewsRequest = repository.getClubsNews(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsList::setValue,
                        error -> {});

        compositeDisposable.add(getClubsNewsRequest);
    }

    public void getNewsDetails(String id){
        Disposable newsDetailsRequest = repository.newsDetails(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsDetails::setValue,
                        error -> {});
        compositeDisposable.add(newsDetailsRequest);
    }

    public void getNewsDetails(String club, String id){
        Disposable getNewsClubDetailsRequest = repository.getNewsClubDetails(club, id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsDetails::setValue,
                        error -> {});
        compositeDisposable.add(getNewsClubDetailsRequest);
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