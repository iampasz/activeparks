package com.app.activeparks.repository;

import android.util.Log;

import com.app.activeparks.data.NetworkModule;
import com.app.activeparks.data.ApiService;
import com.app.activeparks.data.model.Default;
import com.app.activeparks.data.model.authorisation.Authorisation;
import com.app.activeparks.data.model.authorisation.Signup;
import com.app.activeparks.data.model.city.City;
import com.app.activeparks.data.model.clubs.Clubs;
import com.app.activeparks.data.model.clubs.ClubsUserIsMemberModel;
import com.app.activeparks.data.model.clubs.ItemClub;
import com.app.activeparks.data.model.dictionaries.Dictionaries;
import com.app.activeparks.data.model.location.Location;
import com.app.activeparks.data.model.meetings.MeetingsModel;
import com.app.activeparks.data.model.news.ItemNews;
import com.app.activeparks.data.model.news.News;
import com.app.activeparks.data.model.news.NewsClubs;
import com.app.activeparks.data.model.notification.Notifications;
import com.app.activeparks.data.model.qr.QrCodeModel;
import com.app.activeparks.data.model.sportevents.ItemEvent;
import com.app.activeparks.data.model.sportevents.SportEvents;
import com.app.activeparks.data.model.sportsgrounds.ItemSportsground;
import com.app.activeparks.data.model.sportsgrounds.Sportsgrounds;
import com.app.activeparks.data.model.support.Support;
import com.app.activeparks.data.model.support.SupportItem;
import com.app.activeparks.data.model.user.User;
import com.app.activeparks.data.model.user.UserParticipants;
import com.app.activeparks.data.model.uservideo.UserVideo;
import com.app.activeparks.data.model.uservideo.UserVideoItem;
import com.app.activeparks.data.model.video.Video;
import com.app.activeparks.data.model.workout.WorkoutItem;
import com.app.activeparks.data.model.workout.WorkoutModel;
import com.app.activeparks.data.storage.Preferences;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class Repository {

    public Preferences sharedPreferences;
    private String token = "";
    private String userId = "";
    private ApiService service;
    private ApiService serviceSearch = new NetworkModule().getInterfaceSearch();
    private ApiService serviceLocation = new NetworkModule().getInterfacLocation();

    public Repository(Preferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;

        token = "Bearer " + sharedPreferences.getToken();
        userId = sharedPreferences.getId();

        if (service == null) {
            if (sharedPreferences.getServer() == false) {
                service = new NetworkModule().getInterface();
            }else{
                service = new NetworkModule().getTest();
            }
        }
    }

    public Repository() {
        if (service == null) {
            service = new NetworkModule().getInterface();
        }
    }

    public Observable<Video> getVideo(String statusId, String categoryId, String exerciseDifficultyLevelId) {
        return service.getVideo("desc", statusId, categoryId, exerciseDifficultyLevelId);
    }

    public Observable<Video> getVideoUsers(String statusId, String categoryId, String exerciseDifficultyLevelId) {
        return service.getVideoUsers("desc", statusId, categoryId, exerciseDifficultyLevelId);
    }

    public Observable<Video> getVideoRandom(String id, String statusId, String categoryId, String exerciseDifficultyLevelId) {
        return service.getVideoRandom(id, "desc", statusId, categoryId, exerciseDifficultyLevelId);
    }

    public Observable<Dictionaries> getDictionaries() {
        return service.getDictionaries();
    }

    public Observable<Sportsgrounds> sportsgrounds(int limit, String radius, String longitude, String latitude) {
        return service.getSportsgrounds(String.valueOf(limit), radius, longitude, latitude);
    }

    public Observable<ItemSportsground> getSportsground(String id) {
        return service.getSportsground(id);
    }

    public Observable<News> news(int limit) {
        return service.getNews(limit + "");
    }

    public Observable<News> getClubsNews(String id) {
        return service.getClubsNews(id);
    }

    public Observable<ItemNews> newsDetails(String id) {
        return service.getNewsDetails(id);
    }

    public Observable<ItemNews> getNewsClubDetails(String club, String id) {
        return service.getNewsDetails(club, id);
    }

    public Observable<SportEvents> myEvents() {
        Map<String, String> data = new HashMap<>();
        data.put("offset", "0");
        data.put("limit", "30");

        return service.getEvents(token, "/my", data);
    }

    public Observable<SportEvents> myEventsNotifications() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, String> data = new HashMap<>();
        data.put("offset", "0");
        data.put("limit", "10");
        data.put("filters[startsFrom]", dateFormat.format(new Date()));
        data.put("sort[createdAt]", "asc");

        return service.getEvents(token, "/my", data);
    }

    public Observable<SportEvents> getRaitingEvents() {
        return service.getRaitingEvents(token, userId);
    }

    public Observable<SportEvents> getMyResult() {
        Map<String, String> data = new HashMap<>();
        data.put("offset", "0");
        data.put("limit", "20");

        return service.getEvents(token, "/my-results", data);
    }

    public Observable<SportEvents> getClubEvents(String clubId) {
        Map<String, String> data = new HashMap<>();
        data.put("offset", "0");
        data.put("limit", "20");
        data.put("filters[clubId]", clubId);

        return service.getEvents(token, "", data);
    }

    public Observable<SportEvents> eventEstimation(String id, String rating) {

        return service.eventEstimationRequest(token, id, "2", "2");
    }

    public Observable<SportEvents> events(int limit) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, String> data = new HashMap<>();
        data.put("offset", "0");
        data.put("limit", String.valueOf(limit));
        data.put("filters[startsFrom]", dateFormat.format(new Date()));

        return service.getEvents(token, "", data);
    }

    public Observable<SportEvents> eventsHome(int limit) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, String> data = new HashMap<>();
        data.put("filters[startsFrom]", dateFormat.format(new Date()));
        data.put("sort[startsAt]", "asc");
        data.put("offset", "0");
        data.put("limit", String.valueOf(limit));

        return service.getEvents("", data);
    }

    public Observable<ResponseBody> pintRequest(String eventId, String pointId){

        return service.pintRequest(token, eventId, pointId);
    }

    public Observable<WorkoutModel> journal(String userId) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, String> data = new HashMap<>();
        data.put("userId", String.valueOf(userId));
        data.put("direction", "next");
        data.put("date", dateFormat.format(new Date()));
        data.put("offset", "0");
        data.put("limit", "20");

        return service.workout(token, "journal", data);
    }

    public Observable<SportEvents> myevents() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, String> data = new HashMap<>();
        data.put("userId", String.valueOf(userId));
        data.put("direction", "next");
        data.put("date", dateFormat.format(new Date()));
        data.put("offset", "0");
        data.put("limit", "20");

        return service.myevents(token, "journal", data);
    }

    public Observable<WorkoutModel> journal() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, String> data = new HashMap<>();
        data.put("userId", String.valueOf(userId));
        data.put("direction", "next");
        data.put("date", dateFormat.format(new Date()));
        data.put("offset", "0");
        data.put("limit", "20");

        return service.workout(token, "journal", data);
    }

    public Observable<WorkoutModel> addWorkoutNote(String id, String msg) {

        return service.addWorkoutNote(token, "/" + id + "/notes", msg);
    }

    public Observable<WorkoutModel> cangeNotes(String id, String idNotes, String msg) {
        Map<String, String> data = new HashMap<>();
        data.put("title", msg);

        return service.putWrkout(token, "/" + id + "/notes/" + idNotes, data);
    }

    public Observable<WorkoutItem> workout(String id){
        return service.workout(token, id);
    }



    public Observable<WorkoutModel> workoutAdd(String title, boolean isOnce, String weekDays, String startTime, String finishTime, List<String> exercises){
        Map<String, String> data = new HashMap<>();
        data.put("title", title);
        data.put("isActive", "true");
        data.put("type", "Тренування");
        data.put("isOnce", String.valueOf(isOnce));
        data.put("weekDays", "[" + weekDays + "]");
        data.put("startTime", startTime);
        data.put("finishTime", finishTime);
        data.put("exercises", exercises.toString());

        return service.workoutAddRequest(token, data);
    }

    public Observable<WorkoutModel> workoutUpdate(String id, String title, boolean isOnce, String weekDays, String startTime, String finishTime, List<String> exercises){
        Map<String, String> data = new HashMap<>();
        data.put("title", title);
        data.put("isActive", "true");
        data.put("type", "Тренування");
        data.put("isOnce", String.valueOf(isOnce));
        data.put("weekDays", "[" + weekDays + "]");
        data.put("startTime", startTime);
        data.put("finishTime", finishTime);
        data.put("exercises", exercises.toString());

        return service.workoutUpdateRequest(token, id, data);
    }

    public Observable<WorkoutModel> workoutRemove(String id){

        return service.workoutRemoveRequest(token, id);
    }

    public Observable<WorkoutModel> plans(String userId){
        Map<String, String> data = new HashMap<>();
        data.put("userId", String.valueOf(userId));
        data.put("week", "0");
        data.put("offset", "0");
        data.put("limit", "20");

        return service.workout(token, "plan", data);
    }

    public Observable<WorkoutModel> plans(){
        Map<String, String> data = new HashMap<>();
        data.put("userId", String.valueOf(userId));
        data.put("week", "0");
        data.put("offset", "0");
        data.put("limit", "20");

        return service.workout(token, "plan", data);
    }

    public Observable<ItemEvent> getEventDetails(String id) {
        if (token.length() > 10) {
            return service.getEventDetails(token, id);
        }else{
            return service.getEventDetails(id);
        }
    }

    public Observable<MeetingsModel> meetings(String id) {
        return service.meetingsRequest(token, id);
    }

    public Observable<Notifications> notifications() {
        return service.getNotifications(token);

//        List<Notifications> notifications1 = new ArrayList<>();
//        notifications1.add(response.body());
//        sharedPreferences.setNotification(notifications1);

//        if (sharedPreferences.getNotification() != null) {
//            notifications.setValue(sharedPreferences.getNotification().get(0));
//        }
    }

    public Observable<Clubs> getClubsCreator() {
        return service.getClubsCreator(token);
    }

    public Observable<Clubs> getClubsParticipant() {
        return service.getClubsParticipant(token);
    }

    public Observable<Clubs> getClubsAll() {
        return service.getClubsAll(token);
    }

    public Observable<ClubsUserIsMemberModel> getMyClubs() {
        return service.getClubs(token, userId);
    }

    public Observable<ClubsUserIsMemberModel> getClubsUser(String id) {
        return service.getClubs(token, id);
    }


    public Observable<ItemClub> getClubsDetails(String id) {
        return service.getClubsDetails(token, id);
    }

    public Observable<Default> setClubsNews(String id, NewsClubs newsClubs) {
        return service.setClubsNews(token, id, newsClubs);
    }

    public Observable<UserVideo> getUserVideos() {
        return service.getUserVideos(token);
    }

    public Observable<UserParticipants> getEventUser(String id, Boolean user) {
        return service.getEventUser(token, id, user ? "heads" : "participants");
    }

    public Observable<UserParticipants>  getClubsUser(String id, Boolean user) {
        return service.getClubsUser(token, id, user ? "heads" : "members");
    }

    public Observable<UserParticipants> getEventUserApplying(String id) {
        return service.getEventUserApplying(token, id);
    }

    public Observable<UserParticipants> getClubsUserApplying(String id) {
        return service.getClubsUserApplying(token, id);
    }

    public Observable<ResponseBody> applyUser(String id) {
        return service.getApplyUser(token, id);
    }

    public Observable<ResponseBody> approveUser(String id, String user) {
        return service.getApproveUser(token, id, user);
    }

    public Observable<ResponseBody> eventApplyingRequest(String id, String user, String type) {
        return service.eventApplyingRequest(token, user, id, type);
    }

    public Observable<ResponseBody> clubsApplyingRequest(String id, String user, String type) {
        return service.clubsApplyingRequest(token, user, id, type);
    }

    public Observable<ResponseBody> rejectUser(String id) {
        return service.rejectUser(token, id, userId);
    }

    public Observable<ResponseBody> removeUser(String id) {
        return service.removeUser(token, id, userId);
    }

    public Observable<UserVideoItem> createUserVideo() {
        return service.createUserVideo(token);
    }

    public Observable<UserVideoItem> getUserVideo(String id) {
        return service.getUserVideo(token, id);
    }

    public Observable<UserVideoItem> updateUserVideo(String id, UserVideoItem userVideoItem) {
        return service.updateUserVideo(token, id, userVideoItem);
    }

    public Observable<ResponseBody> sendUserVideo(String id) {
        return service.sendUserVideo(token, id);
    }

    public Observable<ResponseBody> deleteUserVideo(String id) {
        return service.deleteUserVideo(token, id);
    }

    public Single<Authorisation> login(String email, String password, String type) {
        return service.login(email, password, type).onErrorResumeNext(throwable -> {
            if (throwable instanceof HttpException) {
                HttpException httpException = (HttpException) throwable;
                if (httpException.code() == 401) {
                    ResponseBody errorBody = httpException.response().errorBody();
                    return Single.error(new Exception(errorBody.string()));
                }
            }
            return Single.error(throwable);
        });
    }

    public Observable<Signup> signup(String nickname, String password, String email, String code) {
        return service.signup(nickname, password, email, code).onErrorResumeNext(throwable -> {
            if (throwable instanceof HttpException) {
                HttpException httpException = (HttpException) throwable;
                if (httpException.code() == 400) {
                    ResponseBody errorBody = httpException.response().errorBody();
                    return Observable.error(new Exception(errorBody.string()));
                }
            }
            return Observable.error(throwable);
        });
    }

    public Observable<ResponseBody> restorePassword(String email, String code, String password) {
        return service.restorePassword(email, code, password)
                .onErrorResumeNext(throwable -> {
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        ResponseBody errorBody = httpException.response().errorBody();
                        return Observable.error(new Exception(errorBody.string()));
                    }
                    return Observable.error(throwable);
                });
    }

    public Single<Default> sendCodeEmail(String code) {
        return service.sendOfCodeEmail(code)
                .onErrorResumeNext(throwable -> {
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        if (httpException.code() == 400) {
                            ResponseBody errorBody = httpException.response().errorBody();
                            return Single.error(new Exception(errorBody.string()));
                        }
                    }
                    return Single.error(throwable);
                });
    }

    public Observable<ResponseBody> sendSmsCode(String phone) {
        return service.sendSmsRequest(token, phone)
                .onErrorResumeNext(throwable -> {
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        ResponseBody errorBody = httpException.response().errorBody();
                        return Observable.error(new Exception(errorBody.string()));
                    }
                    return Observable.error(throwable);
                });
    }

    public Observable<ResponseBody> verifyUserPhoneRequest(String phone, String code) {
        return service.verifyUserPhoneRequest(token, userId, phone, code)
                .onErrorResumeNext(throwable -> {
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        ResponseBody errorBody = httpException.response().errorBody();
                        return Observable.error(new Exception(errorBody.string()));
                    }
                    return Observable.error(throwable);
                });
    }

    public Observable<ResponseBody> verifyUserEmailRequest(String email, String code) {
        return service.verifyUserEmailRequest(token, userId, email, code)
                .onErrorResumeNext(throwable -> {
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        ResponseBody errorBody = httpException.response().errorBody();
                        return Observable.error(new Exception(errorBody.string()));
                    }
                    return Observable.error(throwable);
                });
    }
    public Repository setPush(String token, String pushToken){
       service.setPush("Bearer " + token, pushToken).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
               .subscribe(result -> {}, error -> {});
       return this;
    }

    public Repository updatePushToken(String pushToken){
        Log.d("token_push_update", " "+ pushToken);
        service.setPush(token, pushToken);
        return this;
    }

    public Observable<City> searchCity(String city) {
        return serviceSearch.searchCity(city);
    }

    public Observable<Location> location(String lat, String lon) {
        Map<String, String> data = new HashMap<>();
        data.put("format", "json");
        data.put("accept-language", "ua");
        data.put("lat", lat);
        data.put("lon", lon);
        return serviceLocation.locationRequest(data);
    }

    public Observable<User> getUser() {
        return service.getUser(token, userId).onErrorResumeNext(throwable -> {
            if (throwable instanceof HttpException) {
                HttpException httpException = (HttpException) throwable;
                if (httpException.code() == 401) {
                    return Observable.error(new Exception("401"));
                }
            }
            return Observable.error(throwable);
        });
    }

    public Observable<User> getUser(String id) {
        return service.getUser(token, id + "/free").onErrorResumeNext(throwable -> {
            if (throwable instanceof HttpException) {
                HttpException httpException = (HttpException) throwable;
                if (httpException.code() == 401) {
                    return Observable.error(new Exception("401"));
                }
            }
            return Observable.error(throwable);
        });
    }

    public void userMapper(User user) {
//        if (sharedPreferences.getDictionarie().getDistricts() != null) {
//            for (District district : sharedPreferences.getDictionarie().getDistricts()) {
//                if (district.getId().equals(user.getDistrictId())) {
//                    user.setDistrictId(district.getTitle());
//                }
//            }
//        }
//        if (sharedPreferences.getDictionarie().getRegions() != null) {
//            for (Region region : sharedPreferences.getDictionarie().getRegions()) {
//                if (region.getId().equals(user.getRegionId())) {
//                    user.setRegionId(region.getAlterTitle());
//                }
//            }
//        }
//        mUser.setValue(user);
    }

    public Observable<ResponseBody> updateUser(User user) {
        return service.updateUser(token, userId, user).onErrorResumeNext(throwable -> {
            if (throwable instanceof HttpException) {
                HttpException httpException = (HttpException) throwable;
                if (httpException.code() == 400) {
                    ResponseBody errorBody = httpException.response().errorBody();
                    return Observable.error(new Exception(errorBody.string()));
                }
            }
            return Observable.error(throwable);
        });
    }

    public Observable<User> removeUser() {
        return service.removeUser(token, userId);
    }

    public Observable<Support> getSupportList() {
        return service.getSupportList(token);
//            List<SupportItem> mSupportItem = new ArrayList<>();
//            List<BaseDictionaries> eventHoldingStatuses = sharedPreferences.getDictionarie().getSupportTicketStatuses();
//                        for (SupportItem supportItem : response.body().getItems()) {
//                for (BaseDictionaries base : eventHoldingStatuses) {
//                    if (supportItem.getStatusId().equals(base.getId())) {
//                        supportItem.setStatusId(base.getTitle());
//                    }
//                }
//                mSupportItem.add(supportItem);
//            }
//            Support sportEvents = new Support().setItems(mSupportItem);
//                        mSupport.setValue(sportEvents);
    }

    public Observable<SupportItem> createSupport() {
        return service.createSupport(token);
    }

    public Observable<ResponseBody> updateSupport(String id, SupportItem supportItem) {
        return service.updateSupport(token, id, supportItem);
    }

    public  Observable<SupportItem> sendSupportMessage(String id) {
        return service.sendMessage(token, id);
    }

    public Observable<SupportItem> getSupportDetails(String id) {
        return service.getSupportDetails(token, id);
    }

    public Observable<QrCodeModel> activateClubQrCode(String id) {
        return service.activateClubQrCodeRequest(token, id);
    }

    public Observable<QrCodeModel> activatePointQrCode(String id) {
        return service.activatePointQrCodeRequest(token, id);
    }

    public Observable<QrCodeModel> createQrCodePoint(String eventId, String pointId){
        return service.createQrCodePointRequest(token, eventId + "/points/" + pointId);
    }

    public Observable<ResponseBody> jointLeave(String eventId){
        return service.jointEvent(token, eventId + "/leave", userId, eventId);
    }

    public Observable<ResponseBody> jointEvent(String eventId){
        return service.jointEvent(token, eventId + "/join", userId, eventId);
    }

    public Observable<QrCodeModel> createQrCodeClub(String clubId){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Map<String, String> data = new HashMap<>();
        data.put("title", "qrcode");
        data.put("clubId", clubId);
        data.put("endDateOfUse", dateFormat.format(new Date()));

        return service.createQrCodeClubRequest(token, data);
    }

    public Repository logout() {
        service.logout(token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> {
        }, error -> {
        });
        return this;
    }

    public Observable<Default> updateFile(File file, String type) {
            int random = (int) (Math.random() * 200);
            String name = "file" + random;

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);

            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", file.getName(), requestBody);

            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), type);

            return  service.updateFile(token, name, (int) file.length(), 1,1, name, filename, body);
    }


}
