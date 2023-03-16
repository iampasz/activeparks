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

    public Preferences(){
    }
    @Inject
    public Preferences(Context context){
        mSettings = context.getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE);
    }

    public Preferences Preferences(Context context){
        mSettings = context.getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE);
        return this;
    }

    public void setToken(String value){
        mSettings.edit().putString("token", value).apply();
    }

    public void setId(String id){
        mSettings.edit().putString("id_user", id).apply();
    }

    public void setPushToken(String value){
        mSettings.edit().putString("push_token", value).apply();;
    }

    public String getToken(){
        return mSettings.getString("token", "");
    }

    public String getId(){
        return mSettings.getString("id_user", null);
    }

    public String getPushToken(){
        return mSettings.getString("push_token", null);
    }

    public List<User> getUser() {
        String data = mSettings.getString("user", null);
        Type listType = new TypeToken<ArrayList<User>>(){}.getType();
        Gson gson = new Gson();
        List<User> list = gson.fromJson(data, listType);
        return list != null ? list : null;
    }

    public void setUser(List<User> lstApp) {
        Gson gson = new Gson();
        String data = gson.toJson(lstApp);
        mSettings.edit().putString("user", data).apply();
    }

    public List<Notifications> getNotification() {
        String data = mSettings.getString("notifications", null);
        Type listType = new TypeToken<ArrayList<Notifications>>(){}.getType();
        Gson gson = new Gson();
        List<Notifications> list = gson.fromJson(data, listType);
        return list != null ? list : null;
    }

    public void setNotification(List<Notifications> lstApp) {
        Gson gson = new Gson();
        String data = gson.toJson(lstApp);
        mSettings.edit().putString("notifications", data).apply();
    }

    public List<Dictionaries> getDictionarie() {
        String data = mSettings.getString("dictionaries", null);
        Type listType = new TypeToken<ArrayList<Dictionaries>>(){}.getType();
        Gson gson = new Gson();
        List<Dictionaries> list = gson.fromJson(data, listType);
        return list != null ? list : null;
    }

    public void setDictionarie(List<Dictionaries> lstApp) {
        try{
        Gson gson = new Gson();
        String data = gson.toJson(lstApp);
        mSettings.edit().putString("dictionaries", data).apply();
        }catch (Exception e){}
    }

    public List<SportEvents> getSportEvents() {
        String data = mSettings.getString("sportevents", null);
        Type listType = new TypeToken<ArrayList<SportEvents>>(){}.getType();
        Gson gson = new Gson();
        List<SportEvents> list = gson.fromJson(data, listType);
        return list != null ? list : null;
    }

    public void setSportEvents(List<SportEvents> lstApp) {
        try{
            Gson gson = new Gson();
            String data = gson.toJson(lstApp);
            mSettings.edit().putString("sportevents", data).apply();
        }catch (Exception e){}
    }

    public void clear(){
        mSettings.edit().clear().commit();
    }

}
