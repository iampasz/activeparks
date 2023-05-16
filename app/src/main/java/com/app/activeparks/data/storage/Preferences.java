package com.app.activeparks.data.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.activeparks.data.model.dictionaries.Dictionaries;
import com.app.activeparks.data.model.notification.Notifications;
import com.app.activeparks.data.model.sportevents.SportEvents;
import com.app.activeparks.data.model.user.User;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class Preferences {

    private SharedPreferences mSettings;
    private SharedPreferences mSettingApp;

    public Preferences(){
    }
    @Inject
    public Preferences(Context context){
        mSettings = context.getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE);
        mSettingApp = context.getSharedPreferences("APP_SETTING", Context.MODE_PRIVATE);
    }

    public Preferences Preferences(Context context){
        mSettings = context.getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE);
        mSettingApp = context.getSharedPreferences("APP_SETTING", Context.MODE_PRIVATE);
        return this;
    }

    public void setToken(String value){
        mSettings.edit().putString("token", value).apply();
    }

    public void setId(String id){
        mSettings.edit().putString("id_user", id).apply();
    }

    public void setPushToken(String value){
        mSettingApp.edit().putString("push_token", value).apply();;
    }

    public String getToken(){
        return mSettings.getString("token", null);
    }

    public String getId(){
        return mSettings.getString("id_user", null);
    }

    public String getPushToken(){
        return mSettingApp.getString("push_token", null);
    }

    public User getUser() {
        String data = mSettings.getString("profile", null);
        Gson gson = new Gson();
        User user = gson.fromJson(data, User.class);
        return user != null ? user : null;
    }

    public void setUser(User user) {
        Gson gson = new Gson();
        String data = gson.toJson(user);
        mSettings.edit().putString("profile", data).apply();
    }

    public Notifications getNotification() {
        String data = mSettings.getString("notifications", null);
        Gson gson = new Gson();
        Notifications notifications = gson.fromJson(data, Notifications.class);
        return notifications != null ? notifications : null;
    }

    public void setNotification(Notifications notifications) {
        Gson gson = new Gson();
        String data = gson.toJson(notifications);
        mSettings.edit().putString("notifications", data).apply();
    }

    public Dictionaries getDictionarie() {
        String data = mSettingApp.getString("dictionaries", null);
        Gson gson = new Gson();
        Dictionaries dictionaries = gson.fromJson(data, Dictionaries.class);
        return dictionaries != null ? dictionaries : null;
    }

    public void setDictionarie(Dictionaries dictionaries) {
        try{
        Gson gson = new Gson();
        String data = gson.toJson(dictionaries);
        mSettingApp.edit().putString("dictionaries", data).apply();
        }catch (Exception e){}
    }

    public void setServer(Boolean type){
        mSettingApp.edit().putBoolean("server", type).apply();
    }

    public Boolean getServer(){
        return mSettingApp.getBoolean("server", false);
    }

    public void clear(){
        mSettings.edit().clear().apply();
    }

}
