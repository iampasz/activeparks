package com.app.activeparks.ui.maps;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.repository.Repository;
import com.app.activeparks.data.model.sportsgrounds.Sportsgrounds;

import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MapsViewModel extends ViewModel implements MapEventsReceiver {

    private final MutableLiveData<Sportsgrounds> sportsgroundsList = new MutableLiveData<>();

    private Repository apiRepository;
    private Double lat = 30.51814, lon = 50.44812, myLat = 30.51814, myLon = 50.44812;
    //private Double lat, lon;

    public MapsViewModel() {
        apiRepository = new Repository();
    }

    public LiveData<Sportsgrounds> getSportsground() {
        return sportsgroundsList;
    }

    void getSportsgroundList(int radius){
        apiRepository.sportsgrounds(25,"" + radius, "" + lat, "" + lon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> sportsgroundsList.setValue(result),
                        error -> {});
    }

    void setUpdateSportsgroundList(int radius, double lat, double lon){
        this.lat = lon;
        this.lon = lat;
        getSportsgroundList(radius);
    }

    void setGeolocation(double lat, double lon){
        this.lat = lon;
        this.lon = lat;
    }

    void setMylocation(double lat, double lon){
        this.myLat = lon;
        this.myLon = lat;
    }


    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }
}