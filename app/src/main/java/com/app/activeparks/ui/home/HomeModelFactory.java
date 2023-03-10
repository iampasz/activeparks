package com.app.activeparks.ui.home;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.app.activeparks.data.storage.Preferences;
import com.app.activeparks.ui.result.ResultViewModel;

public class HomeModelFactory implements ViewModelProvider.Factory {

    public HomeModelFactory(Context context){
        sharedPrefsUserRepository = new Preferences(context);
    }

    private Preferences sharedPrefsUserRepository;


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new HomeViewModel(sharedPrefsUserRepository);
    }
}
