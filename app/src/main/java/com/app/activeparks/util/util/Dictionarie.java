package com.app.activeparks.util.util;

import android.content.Context;

import com.app.activeparks.data.storage.Preferences;
import com.app.activeparks.repository.Repository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Dictionarie {

    public Preferences sharedPreferences;

    public Dictionarie init(Context context) {
        sharedPreferences = new Preferences(context);
        new Repository().getDictionaries().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            sharedPreferences.setDictionarie(result);
                        },
                        error -> {
                        });
        return this;
    }
}
