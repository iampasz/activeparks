package com.app.activeparks.ui.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.model.Errors;
import com.app.activeparks.data.repository.Repository;
import com.app.activeparks.data.model.Default;
import com.app.activeparks.data.storage.Preferences;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RegisterViewModel extends ViewModel {


    private Repository repository;
    private MutableLiveData<String> mMessage;
    private MutableLiveData<List<Errors>> mMessageError;

    public RegisterViewModel(Preferences preferences) {
        repository = new Repository(preferences);
        mMessage = new MutableLiveData<>();
        mMessageError = new MutableLiveData<>();
    }

    public LiveData<String> getMessage() {
        return mMessage;
    }

    public LiveData<List<Errors>> getMessageError() {
        return mMessageError;
    }

    public void signup(String nickname, String password, String email, String code){
        repository.signup(nickname, password, email, code).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result.getErrors() == null && result.getId() != null){
                                mMessage.setValue("Успішно зареєстрований");
                                mMessage.setValue("2");
                            }else {
                                mMessage.setValue(result.getError());
                            }
                        },
                        error -> {
                            try {
                                Default def = new Gson().fromJson(error.getMessage(), Default.class);
                                mMessageError.setValue(def.getErrors());
                            } catch (Exception e) {
                                mMessage.setValue("Перевірте підключення до інтернету");
                            }
                        });
    }

    public void sendCode(String email){
        repository.sendCodeEmail(email).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result.getLastRequestTimestamp() != null){
                                mMessage.setValue("Код відправлено!");
                                mMessage.setValue("1");
                            }else {
                                mMessage.setValue("1");
                            }
                        },
                        error -> {
                            try {
                                Default def = new Gson().fromJson(error.getMessage(), Default.class);
                                mMessage.setValue(def.getErrors().get(0).getMsg());
                            } catch (Exception e) {
                                mMessage.setValue("Перевірте підключення до інтернету");
                            }
                        });
    }
}