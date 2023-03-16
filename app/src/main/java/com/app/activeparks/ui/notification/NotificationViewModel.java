package com.app.activeparks.ui.notification;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.repository.Repository;
import com.app.activeparks.data.model.notification.Notifications;
import com.app.activeparks.data.model.sportevents.SportEvents;
import com.app.activeparks.data.storage.Preferences;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class NotificationViewModel extends ViewModel {
    private Repository repository;

    private MutableLiveData<SportEvents> eventsList = new MutableLiveData<>();
    private MutableLiveData<Notifications> notificationsList = new MutableLiveData<>();


    public NotificationViewModel(Preferences sharedPreferences) {
        this.repository = new Repository(sharedPreferences);
    }

    public void update(){
        events();
        notifications();
    }

    public LiveData<Notifications> getNotifications() {
        return notificationsList;
    }

    public LiveData<SportEvents> getSportEventsList() {
        return eventsList;
    }

    public void notifications() {
        repository.notifications().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> notificationsList.setValue(result),
                        error -> {
                        });

    }

    public void events() {
        repository.myEventsNotifications().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> eventsList.setValue(result),
                        error -> {});
    }
}