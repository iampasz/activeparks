package com.app.activeparks.ui.restore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.storage.Preferences;
import com.app.activeparks.data.repository.Repository;
import com.app.activeparks.data.model.Default;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class RestoreViewModel extends ViewModel {

    private Repository repository;
    private MutableLiveData<Default> mMessage;
    private MutableLiveData<Default> mResotoreCode;
    private MutableLiveData<Boolean> closed = new MutableLiveData<>();

    public RestoreViewModel(Preferences preferences) {
        repository = new Repository(preferences);
        mMessage = new MutableLiveData<>();
        mResotoreCode = new MutableLiveData<>();
    }

    public LiveData<Default> getErroMessage() {
        return mMessage;
    }

    public LiveData<Default> getRestoreCode() {
        return mResotoreCode;
    }

    public LiveData<Boolean> getClosed() {
        return closed;
    }

    public void sendCode(String email){
        repository.sendCodeEmail(email)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result != null){
                                mMessage.setValue(result);
                            }
                        },
                        error -> {
                            try {
                                Default def = new Gson().fromJson(error.getMessage(), Default.class);
                                mMessage.setValue(def);
                            }catch (Exception e){
                                mMessage.setValue(new Default("Перевірте підключення до інтернету"));
                            }
                        });
    }

    public void restoreCode(String email, String code, String password){
        repository.restorePassword(email, code, password).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            mMessage.setValue(new Default("Пароль змінено"));
                            closed.setValue(true);
                        },
                        error -> {
                            try {
                                Default def = new Gson().fromJson(error.getMessage(), Default.class);
                                mMessage.setValue(def);
                            } catch (Exception e) {
                                mMessage.setValue(new Default("Перевірте підключення до інтернету"));
                            }
                        });
    }

}