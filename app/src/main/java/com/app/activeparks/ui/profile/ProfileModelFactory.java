package com.app.activeparks.ui.profile;

import static com.app.activeparks.util.ConstKt.SESSION_REPOSITORY;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.app.activeparks.data.repository.sesion.SharedPreferencesMobileApiSessionRepository;
import com.app.activeparks.data.storage.Preferences;

public class ProfileModelFactory implements ViewModelProvider.Factory {

    public ProfileModelFactory(Context context){
        sharedPrefsUserRepository = new Preferences(context);
        newRepository = new SharedPreferencesMobileApiSessionRepository(context.getSharedPreferences(SESSION_REPOSITORY, Context.MODE_PRIVATE));
    }

    public Preferences sharedPrefsUserRepository;

    //TODO update after move one repo
    public SharedPreferencesMobileApiSessionRepository newRepository;


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ProfileViewModelOld(sharedPrefsUserRepository);
        return (T) new ProfileViewModel(sharedPrefsUserRepository, newRepository);
    }
}
