package com.app.activeparks.ui.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.repository.Repository;
import com.app.activeparks.data.model.Default;
import com.app.activeparks.data.model.authorisation.Signup;
import com.app.activeparks.data.storage.Preferences;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RegisterViewModel extends ViewModel {


    private Repository repository;
    private Preferences preferences;
    private MutableLiveData<String> mMessage;

    public RegisterViewModel(Preferences preferences) {
        this.preferences = preferences;
        this.repository = new Repository();
        mMessage = new MutableLiveData<>();
    }

    public LiveData<String> getMessage() {
        return mMessage;
    }

    public void signup(String nickname, String password, String email, String code){
        repository.signup(nickname, password, email, code).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result.getError() != null && result.getId() != null){
                                //preferences.setToken(authorisation.getToken());
                            }else {
                                mMessage.setValue(result.getError());
                            }
                        },
                        error -> mMessage.setValue("Перевірте підключення до інтернету"));
    }

    public void sendCode(String email){
        repository.sendCodeEmail(email).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result.getError() != null){
                                //preferences.setToken(authorisation.getToken());
                            }else {
                                mMessage.setValue(result.getError());
                            }
                        },
                        error -> mMessage.setValue("Перевірте підключення до інтернету"));
    }
}