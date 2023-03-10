package com.app.activeparks.ui.event;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.model.meetings.MeetingsModel;
import com.app.activeparks.repository.Repository;
import com.app.activeparks.data.model.dictionaries.BaseDictionaries;
import com.app.activeparks.data.model.event.RoutePoint;
import com.app.activeparks.data.model.sportevents.ItemEvent;
import com.app.activeparks.data.model.sportevents.SportEvents;
import com.app.activeparks.data.storage.Preferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class EventViewModel extends ViewModel {

    private Preferences preferences;
    private MutableLiveData<ItemEvent> mItemEvent;
    private MutableLiveData<SportEvents> mSportEvents;
    private MutableLiveData<String> location = new MutableLiveData<>();
    private MutableLiveData<List<MeetingsModel.MeetingItem>> mMeeting = new MutableLiveData<>();
    public List<RoutePoint> routePoints = new ArrayList<>();
    private ArrayList<ItemEvent> mFilterSportEvent = new ArrayList<>();
    private List<BaseDictionaries> eventHoldingStatuses = new ArrayList<>();
    public List<ItemEvent> mSportEvent = new ArrayList<>();
    public String mId;

    public Boolean isCoordinator = false;

    private Repository repository;

    public EventViewModel(Preferences preferences) {
        this.preferences = preferences;
        repository = new Repository(preferences);
        mItemEvent = new MutableLiveData<>();
        mSportEvents = new MutableLiveData<>();
        eventHoldingStatuses = preferences.getDictionarie().get(0).getEventHoldingStatuses();
    }

    public LiveData<ItemEvent> getEventDetails() {
        return mItemEvent;
    }

    public LiveData<SportEvents> getSportEventsList() {
        return mSportEvents;
    }

    public LiveData<String> getLocation() {
        return location;
    }

    public LiveData<List<MeetingsModel.MeetingItem>> getMeetingRecords() {
        return mMeeting;
    }

    public void getEvent(String id) {
        mId = id;
        repository.getEventDetails(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            mItemEvent.setValue(result);
                            if (result.getRoutePoints() != null) {
                                routePoints = result.getRoutePoints();
                                Collections.sort(routePoints);
                            }
                            if (result.getEventUser().getIsCoordinator() != null) {
                                isCoordinator = result.getEventUser().getIsCoordinator();
                            }
                        },
                        error -> {
                        });
    }

    public void getUpdateEvent() {
        if (mId != null) {
            getEvent(mId);
        }
    }

    public void getSportEvents() {
        repository.events(20).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> statusMapper(result.getItems()),
                        error -> {
                        });
    }

    public void getSportEvents(String id) {
        repository.getClubEvents(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> statusMapper(result.getItems()),
                        error -> {
                        });
    }

    public void applyUser() {
        if (mItemEvent.getValue().getEventUser() == null) {
            repository.jointEvent(mId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> getEvent(mId),
                    error -> {
                    });
        } else {
            if (mItemEvent.getValue().getEventUser().getIsApproved() == true) {
                repository.jointLeave(mId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> getEvent(mId),
                                error -> {
                                });
            }
        }
    }

    public void meetingRecords() {
        repository.meetings(mId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> mMeeting.setValue(result.getItems()),
                        error -> {
                        });
    }

    public void statusMapper(List<ItemEvent> itemEvent) {
        for (ItemEvent itemEvent1 : itemEvent) {
            for (BaseDictionaries eventHoldingStatuses : eventHoldingStatuses) {
                if (itemEvent1.getHoldingStatusId().equals(eventHoldingStatuses.getId())) {
                    itemEvent1.setHoldingStatusId(eventHoldingStatuses.getTitle());
                }
            }
            mSportEvent.add(itemEvent1);
        }
        SportEvents sportEvents = new SportEvents().setItems(mSportEvent);
        mSportEvents.setValue(sportEvents);
    }

    public String statusMapper(String status) {
        for (BaseDictionaries eventHoldingStatuses : eventHoldingStatuses) {
            if (status.equals(eventHoldingStatuses.getId())) {
                return eventHoldingStatuses.getTitle();
            }
        }
        return "не відомо";
    }

    public SportEvents filterData(String data) {
        mFilterSportEvent.clear();
        for (ItemEvent itemEvent : mSportEvent) {
            if (itemEvent.getStartsAt() != null &&
                    itemEvent.getStartsAt().length() > 10 &&
                    data.equals(itemEvent.getStartsAt().substring(0, 10))) {
                mFilterSportEvent.add(itemEvent);
            }
        }
        return new SportEvents().setItems(mFilterSportEvent);
    }

    public Boolean typeId(String status) {
        return status.equals("e58e5c86-5ca7-412f-94f0-88effd1a45a8");
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
            for (RoutePoint items : mItemEvent.getValue().getRoutePoints()) {
                double roundedLan = Math.round(items.getLocation().get(0) * 1000 / 1000);
                double roundedLon = Math.round(items.getLocation().get(1) * 1000 / 1000.0);
                if (roundedLan == lat && roundedLon == lon) {
                    if (items.getId() != null){
                        pointPass(items.getId());
                    }
                }
            }
        }
    }

    public void pointPass(String id) {
        repository.pintRequest(mId, id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            getEvent(mId);
                        },
                        error -> {
                            getEvent(mId);
                        });
    }

    public boolean getUserAuth() {
        return preferences.getToken().length() > 1 ? true : false;
    }
}