package com.app.activeparks.ui.event.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.model.calendar.CalendarModel;
import com.app.activeparks.data.model.dictionaries.BaseDictionaries;
import com.app.activeparks.data.model.meetings.MeetingsModel;
import com.app.activeparks.data.model.points.RoutePoint;
import com.app.activeparks.data.model.sportevents.ItemEvent;
import com.app.activeparks.data.model.sportevents.SportEvents;
import com.app.activeparks.data.repository.Repository;
import com.app.activeparks.data.storage.Preferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class EventViewModel extends ViewModel {

    private final MutableLiveData<ItemEvent> mItemEvent;
    private final MutableLiveData<SportEvents> mSportEvents;
    private final MutableLiveData<String> location = new MutableLiveData<>();
    private final MutableLiveData<CalendarModel> calendar = new MutableLiveData<>();
    private final MutableLiveData<List<MeetingsModel.MeetingItem>> mMeeting = new MutableLiveData<>();
    private List<BaseDictionaries> eventHoldingStatuses = new ArrayList<>();
    private final List<ItemEvent> mSportEvent = new ArrayList<>();
    public String mId;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();


    public RoutePoint address;

    public Map<String, String> filterLongitude = new HashMap<>();

    public String eventSelectDay = "";
    public int eventFilter = 1;

    public Boolean isCoordinator = false;

    private final Repository repository;

    public EventViewModel(Preferences preferences) {
        repository = new Repository(preferences);
        mItemEvent = new MutableLiveData<>();
        mSportEvents = new MutableLiveData<>();

        try {
            if (preferences.getDictionarie() != null) {
                eventHoldingStatuses = preferences.getDictionarie().getEventHoldingStatuses();
            }
        } catch (Exception ignored) {
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
        Disposable eventDetails = repository.getEventDetails(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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

        compositeDisposable.add(eventDetails);
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
        Disposable calendarEventRequest = repository.calendarEventRequest(date, id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result != null) {
                                calendar.setValue(result);
                            }
                        },
                        error -> {
                        });
        compositeDisposable.add(calendarEventRequest);
    }

    public void getEventsList() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        eventsDay(dateFormat.format(new Date()));
    }

    public void getEventsList(String clubId) {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        getEvents(dateFormat.format(new Date()), clubId);
    }

    public void getEvents(String date, String clubId) {
        Disposable events = repository.events(30, date, clubId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> statusMapper(result.getItems()),
                        error -> {
                        });

        compositeDisposable.add(events);
    }

    public void eventsDay(String date) {
        eventSelectDay = date;

        Map<String, String> data = new HashMap<>();
        data.put("offset", "0");
        data.put("limit", "10");
        data.put("filters[startsFrom]", date);
        data.put("filters[startsTo]", date);

        if (eventFilter == 0) {
            Log.d("test_log", "0" + filterLongitude.toString());
            data.putAll(filterLongitude);
        } else if (eventFilter == 1) {
            data.put("sort[startsAt]", "asc");
        } else if (eventFilter == 2) {
            data.put("sort[startsAt]", "asc");
            data.put("filters[holdingstatus]", "active");
        }
        Disposable eventsDay = repository.eventsDay(data).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> statusMapper(result.getItems()),
                        error -> {
                        });

        compositeDisposable.add(eventsDay);
    }

    public void applyUser() {
        if (Objects.requireNonNull(mItemEvent.getValue()).getEventUser() == null) {
            Disposable jointEvent = repository.jointEvent(mId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> getEvent(mId),
                    error -> {
                    });
            compositeDisposable.add(jointEvent);
        } else {
            if (mItemEvent.getValue().getEventUser().getIsApproved()) {
                Disposable jointLeave = repository.jointLeave(mId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> getEvent(mId),
                                error -> {
                                });
                compositeDisposable.add(jointLeave);
            }
        }
    }

    public void setEstimation(float raiting) {
        Disposable eventEstimation = repository.eventEstimation(mId, "" + Math.round(raiting)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> getUpdateEvent(),
                        error -> {
                        });
        compositeDisposable.add(eventEstimation);
    }

    public void meetingRecords() {
        Disposable meetings = repository.meetings(mId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> mMeeting.setValue(result.getItems()),
                        error -> {
                        });
        compositeDisposable.add(meetings);
    }

    public void startEvent() {
        if (Objects.requireNonNull(mItemEvent.getValue()).getRouteStartAt() != null) {
            Disposable eventPostRequest = repository.eventPostRequest(mId, "stop-route").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> getUpdateEvent(),
                            error -> getUpdateEvent());
            compositeDisposable.add(eventPostRequest);
        } else {
            Disposable eventPostRequest = repository.eventPostRequest(mId, "start-route").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> getUpdateEvent(),
                            error -> getUpdateEvent());
            compositeDisposable.add(eventPostRequest);
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
        Disposable locationRepo = repository.location("" + lat, "" + lon).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result.getAddress() != null) {
                                String address = "";
                                if (result.getAddress().getCity() != null) {
                                    address = address + result.getAddress().getCity() + ", ";
                                }
                                if (result.getAddress().getState() != null) {
                                    address = address + result.getAddress().getState() + ", ";
                                }
                                if (result.getAddress().getDistrict() != null) {
                                    address = address + result.getAddress().getDistrict() + ", ";
                                }
                                if (result.getAddress().getTown() != null) {
                                    address = address + result.getAddress().getTown() + ", ";
                                }
                                if (result.getAddress().getRoad() != null) {
                                    address = address + result.getAddress().getRoad();
                                }
                                location.setValue(address);
                            } else {

                                location.setValue(result.getDisplayName());
                            }
                        },
                        error -> location.setValue("Київ"));
        compositeDisposable.add(locationRepo);
    }

    public void filterCordinate(double latitude, double longitude) {
        filterLongitude.put("filters[longitude]", String.valueOf(longitude));
        filterLongitude.put("filters[latitude]", String.valueOf(latitude));
        filterLongitude.put("sort[distancetopointsort]", "asc");
    }

    public void selectFilter(int index) {
        eventFilter = index;
        eventsDay(eventSelectDay);
    }

    public boolean getUserAuth() {
        return !repository.sharedPreferences.getToken().isEmpty();
    }
}