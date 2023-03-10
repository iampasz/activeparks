package com.app.activeparks.ui.result;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.app.activeparks.data.storage.Preferences;

public class ResultModelFactory implements ViewModelProvider.Factory {

    public ResultModelFactory(Context context){
        sharedPrefsUserRepository = new Preferences(context);
    }

    public Preferences sharedPrefsUserRepository;


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ResultViewModel(sharedPrefsUserRepository);
    }
}
