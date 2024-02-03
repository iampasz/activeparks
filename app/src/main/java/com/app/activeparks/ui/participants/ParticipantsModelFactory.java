package com.app.activeparks.ui.participants;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.app.activeparks.data.storage.Preferences;

public class ParticipantsModelFactory implements ViewModelProvider.Factory {

    public ParticipantsModelFactory(Context context) {
        sharedPrefsUserRepository = new Preferences(context);
    }

    public Preferences sharedPrefsUserRepository;


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ParticipantsViewModelOld(sharedPrefsUserRepository);
    }
}