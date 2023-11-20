package com.app.activeparks.ui.result;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.repository.Repository;
import com.app.activeparks.data.model.notification.Notifications;
import com.app.activeparks.data.storage.Preferences;

public class ResultViewModel extends ViewModel {
    private Repository apiRepository;
    private MutableLiveData<Notifications> mResult;

    public ResultViewModel(Preferences sharedPreferences)   {
        this.apiRepository = new Repository(sharedPreferences);
        this.mResult = new MutableLiveData<>();
        getResultList();
    }

    public LiveData<Notifications> getResult() {
        return mResult;
    }

    public void getResultList(){
    }
}