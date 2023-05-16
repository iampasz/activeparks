package com.app.activeparks.ui.event;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.model.calendar.CalendarModel;
import com.app.activeparks.data.model.points.RoutePoint;
import com.app.activeparks.data.model.meetings.MeetingsModel;
import com.app.activeparks.repository.Repository;
import com.app.activeparks.data.model.dictionaries.BaseDictionaries;
import com.app.activeparks.data.model.sportevents.ItemEvent;
import com.app.activeparks.data.model.sportevents.SportEvents;
import com.app.activeparks.data.storage.Preferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class EventViewModel extends ViewModel {

    private MutableLiveData<ItemEvent> mItemEvent;
    private MutableLiveData<SportEvents> mSportEvents;
    private MutableLiveData<String> location = new MutableLiveData<>();
    private MutableLiveData<CalendarModel> calendar = new MutableLiveData<>();
    private MutableLiveData<List<MeetingsModel.MeetingItem>> mMeeting = new MutableLiveData<>();
    private List<BaseDictionaries> eventHoldingStatuses = new ArrayList<>();
    public List<ItemEvent> mSportEvent = new ArrayList<>();
    public String mId;

    public RoutePoint address;

    public Boolean isCoordinator = false;

    private Repository repository;

    public EventViewModel(Preferences preferences) {
        repository = new Repository(preferences);
        mItemEvent = new MutableLiveData<>();
        mSportEvents = new MutableLiveData<>();

        try {
            if (preferences.getDictionarie() != null) {
                eventHoldingStatuses = preferences.getDictionarie().getEventHoldingStatuses();
            }
        } catch (Exception e) {
        }
    }

    public LiveData<ItemEvent> getEventDetails() {
        return mItemEvent;
    }

    public LiveData<SportEvents> getSportEventsList() {
        return mSportEvents;
    }

    public LiveData<CalendarModel> getCalendar() {
        return calendar;
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
                            for (RoutePoint item : result.getRoutePoints()) {
                                if (item.getPointIndex() == 0) {
                                    address = item;
                                    break;
                                }
                            }
                            mItemEvent.setValue(result);
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

    public void calendarEvent() {
        calendarEvent("");
    }

    public void calendarEvent(String id) {
        calendarEvent(new Date(), id);
    }

    public void calendarEvent(Date date, String id) {
        repository.calendarEventRequest(date, id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result != null) {
                                calendar.setValue(result);
                            }
                        },
                        error -> {
                        });
    }

    public void getEventsList() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        eventsDay(dateFormat.format(new Date()));
    }

    public void getEventsList(String clubId) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        getEvents(dateFormat.format(new Date()), clubId);
    }

    public void getEvents(String date) {
        getEvents(date, "");
    }

    public void getEvents(String date, String clubId) {
        repository.events(30, date, clubId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            statusMapper(result.getItems());
                        },
                        error -> {
                        });
    }

    public void eventsDay(String date) {
        repository.eventsDay(10, date, "").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            statusMapper(result.getItems());
                        },
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

    public void setEstimation(float raiting) {
        repository.eventEstimation(mId, "" + Math.round(raiting)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> getUpdateEvent(),
                        error -> {
                        });
    }

    public void meetingRecords() {
        repository.meetings(mId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> mMeeting.setValue(result.getItems()),
                        error -> {
                        });
    }

    public void startEvent() {
        if (mItemEvent.getValue().getRouteStartAt() != null) {
            repository.eventPostRequest(mId, "stop-route").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> getUpdateEvent(),
                            error -> getUpdateEvent());
        } else {
            repository.eventPostRequest(mId, "start-route").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> getUpdateEvent(),
                            error -> getUpdateEvent());
        }
    }

    public void statusMapper(List<ItemEvent> itemEvent) {
        mSportEvent.clear();
        for (ItemEvent itemEvent1 : itemEvent) {
            for (BaseDictionaries eventHoldingStatuses : eventHoldingStatuses) {
                if (itemEvent1.getHoldingStatusId().equals(eventHoldingStatuses.getId())) {
                    itemEvent1.setHoldingStatusText(eventHoldingStatuses.getTitle());
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


    public Boolean typeId(String status) {
        return status.equals("e58e5c86-5ca7-412f-94f0-88effd1a45a8");
    }

    public void location(double lat, double lon) {
        repository.location("" + lat, "" + lon).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result.getAddress() != null){
                                String address = "";
                                if (result.getAddress().getCity() != null){
                                    address = address + result.getAddress().getCity() + ", ";
                                }
                                if (result.getAddress().getState() != null){
                                    address = address + result.getAddress().getState() + ", ";
                                }
                                if (result.getAddress().getDistrict() != null){
                                    address = address + result.getAddress().getDistrict() + ", ";
                                }
                                if (result.getAddress().getTown() != null){
                                    address = address + result.getAddress().getTown() + ", ";
                                }
                                if (result.getAddress().getRoad() != null){
                                    address = address + result.getAddress().getRoad();
                                }
                                location.setValue(address);
                            }else {

                                location.setValue(result.getDisplayName());
                            }
                        },
                        error -> {
                            location.setValue("Київ");
                        });
    }


    public boolean getUserAuth() {
        return !repository.sharedPreferences.getToken().isEmpty();
    }
}