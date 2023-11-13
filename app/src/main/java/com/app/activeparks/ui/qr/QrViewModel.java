package com.app.activeparks.ui.qr;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.storage.Preferences;
import com.app.activeparks.data.repository.Repository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class QrViewModel extends ViewModel {

    private Preferences sharedPreferences;
    private Repository repository;

    private MutableLiveData<String> qr = new MutableLiveData<>();

    public QrViewModel(Preferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        this.repository = new Repository(sharedPreferences);
    }

    public LiveData<String> getQrCode() {
        return qr;
    }

    public void createQrCodeClub(String id) {
        repository.createQrCodeClub(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> qr.setValue(result.getQrCode()),
                        error -> {
                            Log.d("test_qr", "msg " + error.getMessage());}
                );
    }

    public void createQrCodePoint(String eventId, String pointId) {
        repository.createQrCodePoint(eventId, pointId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> qr.setValue(result.getQrCode()),
                        error -> {}
                );
    }
}
