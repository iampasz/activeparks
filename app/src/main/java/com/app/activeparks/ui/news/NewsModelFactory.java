package com.app.activeparks.ui.news;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.app.activeparks.data.storage.Preferences;

public class NewsModelFactory implements ViewModelProvider.Factory {

    public NewsModelFactory(Context context){
        sharedPrefsUserRepository = new Preferences(context);
    }

    public Preferences sharedPrefsUserRepository;


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new NewsViewModel(sharedPrefsUserRepository);
    }
}
