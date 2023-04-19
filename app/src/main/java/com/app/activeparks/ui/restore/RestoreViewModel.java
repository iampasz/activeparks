package com.app.activeparks.ui.restore;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.repository.Repository;
import com.app.activeparks.data.model.Default;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;


public class RestoreViewModel extends ViewModel {

    private Repository repository;
    private MutableLiveData<Default> mMessage;
    private MutableLiveData<Default> mResotoreCode;

    public RestoreViewModel() {
        repository = new Repository();
        mMessage = new MutableLiveData<>();
        mResotoreCode = new MutableLiveData<>();
    }

    public LiveData<Default> getErroMessage() {
        return mMessage;
    }

    public LiveData<Default> getRestoreCode() {
        return mResotoreCode;
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