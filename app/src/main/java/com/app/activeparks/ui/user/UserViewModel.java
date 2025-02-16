package com.app.activeparks.ui.user;

import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.repository.Repository;
import com.app.activeparks.data.storage.Preferences;


public class UserViewModel extends ViewModel {

    private final Preferences sharedPreferences;
    private Repository apiRepository;

    UserViewModel(Preferences sharedPreferences)   {
        this.sharedPreferences = sharedPreferences;
        this.apiRepository = new Repository(sharedPreferences);
    }


    public boolean getUserAuth() {
        if (sharedPreferences.getToken() != null && sharedPreferences.getToken().length() > 1) {
            return true;
        }
        return false;
    }

}