package com.app.activeparks.util;

import android.content.Context;

import com.app.activeparks.repository.Repository;
import com.app.activeparks.data.model.dictionaries.Dictionaries;
import com.app.activeparks.data.storage.Preferences;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Dictionarie {

    public Preferences sharedPreferences;

    public Dictionarie init(Context context) {
        sharedPreferences = new Preferences(context);
        new Repository().getDictionaries().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            List<Dictionaries> dictionarie = new ArrayList<>();
                            dictionarie.add(result);
                            sharedPreferences.setDictionarie(dictionarie);
                        },
                        error -> {
                        });
        return this;
    }
}
