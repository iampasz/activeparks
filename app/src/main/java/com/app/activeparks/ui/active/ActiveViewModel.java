package com.app.activeparks.ui.active;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.activeparks.repository.Repository;

import org.osmdroid.util.GeoPoint;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ActiveViewModel extends AndroidViewModel {

    private Repository apiRepository = new Repository(getApplication().getApplicationContext());
    private MutableLiveData<Boolean> _onSuccess;
    LiveData<Boolean> onSuccess;

    private MutableLiveData<List<List<GeoPoint>>> _listsGeoPoint;
    LiveData<List<List<GeoPoint>>> listsGeoPoint;

    public ActiveViewModel(@NonNull Application application) {
        super(application);
        _onSuccess = new MutableLiveData<>(false);
        onSuccess = _onSuccess;

        _listsGeoPoint = new MutableLiveData<>(Collections.emptyList());
        listsGeoPoint = _listsGeoPoint;
    }


    public void insertGeoPointList(List<GeoPoint> list) {
        apiRepository.insertListGeoPoint(list).subscribe(
                () -> _onSuccess.setValue(true),
                throwable -> _onSuccess.setValue(false)
        );
    }

    public void getListGeoPoint() {
        apiRepository.getActivitiesList().subscribe((geoPointLists, throwable) -> {
            List<List<GeoPoint>> list = geoPointLists.stream().map(geoPoints -> geoPoints.getList().stream().map(geoPointEntity ->
                    new GeoPoint(geoPointEntity.getaLatitude(), geoPointEntity.getaLongitude())
            ).collect(Collectors.toList())).collect(Collectors.toList());
            _listsGeoPoint.postValue(list);
        });
    }


}