package com.app.activeparks.ui.training;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.app.activeparks.data.storage.Preferences;
import com.app.activeparks.ui.support.SupportViewModel;

public class TrainingModelFactory implements ViewModelProvider.Factory {

    public TrainingModelFactory(Context context){
        sharedPrefsUserRepository = new Preferences(context);
    }

    public Preferences sharedPrefsUserRepository;


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TrainingViewModel(sharedPrefsUserRepository);
    }
}
