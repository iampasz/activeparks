package com.app.activeparks.ui.routepoint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.model.points.RoutePoint;
import com.app.activeparks.data.model.sportevents.ItemEvent;
import com.app.activeparks.data.storage.Preferences;
import com.app.activeparks.repository.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class RoutePointViewModel extends ViewModel {

    private MutableLiveData<ItemEvent> routePoint;
    private MutableLiveData<String> location = new MutableLiveData<>();
    private MutableLiveData<String> notification = new MutableLiveData<>();

    public List<RoutePoint> routePointsMap = new ArrayList<>();
    public List<RoutePoint> routePoints = new ArrayList<>();
    public String mId;

    public Boolean isCoordinator = false;

    private Repository repository;

    public RoutePointViewModel(Preferences preferences) {
        repository = new Repository(preferences);
        routePoint = new MutableLiveData<>();
    }

    public LiveData<ItemEvent> getRoutePoint() {
        return routePoint;
    }

    public LiveData<String> getLocation() {
        return location;
    }

    public LiveData<String> getNotification() {
        return notification;
    }

    public void getRoutePoint(String id) {
        mId = id;
        repository.getEventDetails(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result.getRoutePoints() != null) {
                                List<RoutePoint> items = result.getRoutePoints();

                                Collections.sort(items, new Comparator<RoutePoint>() {
                                    @Override
                                    public int compare(RoutePoint item1, RoutePoint item2) {
                                        return Integer.compare(item1.getPointIndex(), item2.getPointIndex());
                                    }
                                });

                                routePointsMap = items;

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    routePoints = items.stream()
                                            .filter(item -> item.getType() == 0)
                                            .collect(Collectors.toList());
                                } else {
                                    routePoints = items;
                                }

                            }
                            routePoint.setValue(result);
                            if (result.getEventUser() != null) {
                                if (result.getEventUser().getIsCoordinator() == true || result.getEventUser().getIsActing() == true) {
                                    isCoordinator = true;
                                }
                            }
                        },
                        error -> {
                        });
    }

    public void getUpdate() {
        if (mId != null) {
            getRoutePoint(mId);
        }
    }


    public void location(double lat, double lon) {
        repository.location("" + lat, "" + lon).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            location.setValue(result.getDisplayName());
                        },
                        error -> {
                            location.setValue("Київ");
                        });
    }

    public void routePoints(double lat, double lon) {
        if (routePoints.size() > 0) {
            for (int index = 0, size = routePoints.size(); index < size; index++) {
                RoutePoint items = routePoint.getValue().getRoutePoints().get(index);
                double roundedLan = lat > items.getLocation().get(0) ? lat - items.getLocation().get(0) : items.getLocation().get(0) - lat;
                double roundedLon = lon > items.getLocation().get(1) ? lon - items.getLocation().get(1) : items.getLocation().get(1) - lon;

                if ((roundedLan <= 0.0002 && roundedLon <= 0.0002) && (index == 0 ? true : routePoints.get(index <= 0 ? index : index - 1).getPassedPoints() == true)) {
                    if (items.getPassedPoints() == false && startsAt() == true) {
                        pointPass(items.getId(), items.getPointIndex());
                    }
                }
            }
        }
    }

    public void pointPass(String id, int point) {
        repository.pintRequest(mId, id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            getUpdate();
                            localNotification(point);
                        },
                        error -> {
                            getUpdate();
                        });
    }

    public void localNotification(int point) {
        String msg;
        if (point == 0){
            msg = "Ви пройшли точку \"Старт\"";
        }else if (point == routePoints.size()){
            msg = "Ви пройшли весь маршрут";
        }else{
            msg = "Ви пройшли точку " + point;
        }
        notification.setValue(msg);
    }

    public boolean startsAt() {
        if (routePoint.getValue().getStartsAt() != null) {
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startsAtDate;
            try {
                startsAtDate = dateFormat.parse(routePoint.getValue().getStartsAt());
                return currentDate.compareTo(startsAtDate) >= 0;
            } catch (ParseException e) {
                return false;
            }
        } else {
            return false;
        }
    }

}