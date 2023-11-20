package com.app.activeparks.ui.scaner;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.repository.Repository;
import com.app.activeparks.data.storage.Preferences;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ScanerViewModel extends ViewModel {

    private Preferences sharedPreferences;
    private Repository repository;
    private MutableLiveData<String> clubQrCode = new MutableLiveData<>();
    private MutableLiveData<String> pointQrCode = new MutableLiveData<>();
    private MutableLiveData<String> selectVideoQrCode = new MutableLiveData<>();
    private MutableLiveData<String> message = new MutableLiveData<>();

    public ScanerViewModel(Preferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        this.repository = new Repository(sharedPreferences);
    }

    public LiveData<String> getClubQrCode() {
        return clubQrCode;
    }
    public LiveData<String> getPointQrCode() {
        return pointQrCode;
    }
    public LiveData<String> getSelectVideoQrCode() {
        return selectVideoQrCode;
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public void activateClubQrCode(String id) {
        repository.activateClubQrCode(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> clubQrCode.setValue(result.getClubId()),
                        error -> clubQrCode.setValue(null)
                );
    }

    public void activateScanerCodeRequest(String id) {
        repository.activateScanerCodeRequest(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> selectVideoQrCode.setValue(result.getId()),
                        error -> selectVideoQrCode.setValue(null)
                );
    }

    public void activatePointQrCode(String id) {
        repository.activatePointQrCode(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> pointQrCode.setValue(result.getSportEventId()),
                        error -> pointQrCode.setValue(null)
                );
    }

    public Boolean getUser() {
        return sharedPreferences.getToken() != null;
    }
}
