package com.app.activeparks.ui.auth;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.repository.Repository;
import com.app.activeparks.data.model.Default;
import com.app.activeparks.data.storage.Preferences;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AuthViewModel extends ViewModel {

    private Repository repository;
    private Preferences preferences;
    private MutableLiveData<Default> mMessage;

    public AuthViewModel(Preferences preferences) {
        this.preferences = preferences;
        this.repository = new Repository(preferences);
        this.mMessage = new MutableLiveData<>();
    }

    public LiveData<Default> getMessage() {
        return mMessage;
    }

    public void login(String email, String password) {



       Disposable login =  repository.login(email, password, "1").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result.getError() == null && result.getToken() != null) {
                                preferences.setToken(result.getToken());
                                preferences.setId(result.getPayload().getId());
                                if (preferences.getPushToken() != null) {
                                    new Repository(preferences).setPush(preferences.getPushToken());
                                }
                                mMessage.setValue(new Default(true));
                                Log.i("LOGIN","Good");
                            } else {
                                mMessage.setValue(new Default(result.getError()));
                            }
                        },
                        error -> {
                            try {
                                Default def = new Gson().fromJson(error.getMessage(), Default.class);
                                mMessage.setValue(def);
                            } catch (Exception e) {
                                mMessage.setValue(new Default("Перевірте підключення до інтернету"));
                                Log.i("LOGIN","Перевірте підключення до інтернету");
                            }
                        }
                );
    }

    public void setServer(Boolean type) {
        preferences.setServer(type);
        repository = new Repository(preferences);
    }

    public Boolean getServer() {
        return preferences.getServer();
    }
}