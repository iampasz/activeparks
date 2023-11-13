package com.app.activeparks.data.network;

import com.app.activeparks.data.model.Default;
import com.app.activeparks.data.model.authorisation.Authorisation;
import com.app.activeparks.data.model.authorisation.Signup;
import com.app.activeparks.data.model.calendar.CalendarModel;
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
import com.app.activeparks.data.model.qr.ScanerResultModel;
import com.app.activeparks.data.model.sportevents.ItemEvent;
import com.app.activeparks.data.model.sportevents.SportEvents;
import com.app.activeparks.data.model.sportsgrounds.ItemSportsground;
import com.app.activeparks.data.model.sportsgrounds.Sportsgrounds;
import com.app.activeparks.data.model.support.Support;
import com.app.activeparks.data.model.support.SupportItem;
import com.app.activeparks.data.model.user.User;
import com.app.activeparks.data.model.user.UserParticipants;
import com.app.activeparks.data.model.user.UserUpdate;
import com.app.activeparks.data.model.uservideo.UserVideo;
import com.app.activeparks.data.model.uservideo.UserVideoItem;
import com.app.activeparks.data.model.video.Video;
import com.app.activeparks.data.model.workout.PlanModel;
import com.app.activeparks.data.model.workout.WorkoutItem;
import com.app.activeparks.data.model.workout.WorkoutModel;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {

    //Авторизація
    @POST("/api/v1/auth/login")
    @FormUrlEncoded
    Single<Authorisation> login(@Field("email") String email, @Field("password") String password, @Field("typeId") String type);

    //Регистрація
    @POST("/api/v1/auth/signup")
    @FormUrlEncoded
    Observable<Signup> signup(@Field("nickname") String nickname, @Field("password") String password, @Field("email") String email, @Field("code") String code);

    //Новини
    @GET("/api/v1/news?offset=0&sort[publishedAt]=desc")
    Observable<News> getNews(@Query("limit") String limit);

    //Новини Клуба
    @GET("/api/v1/clubs/{id}/news?offset=0&limit=20")
    Observable<News> getClubsNews(@Header("Authorization") String token, @Path("id") String id);

    //Новини
    @GET("/api/v1/news/{id}")
    Observable<ItemNews> getNewsDetails(@Path("id") String id);

    //Новини клуба
    @GET("/api/v1/clubs/{club}/news/{id}")
    Observable<ItemNews> getNewsDetails(@Header("Authorization") String token, @Path("club") String club, @Path("id") String id);

    //Заходи
    @GET("/api/v1/sport-events/all?offset=0&limit=270")
    Observable<CalendarModel> calendarEventRequest(@QueryMap Map<String, String> options);

    @PUT("/api/v1/sport-events/{id}/set-estimation")
    @FormUrlEncoded
    Observable<SportEvents> eventEstimationRequest(@Header("Authorization") String token, @Path(value = "id", encoded = true) String url, @Field("id") String id, @Field("eventEstimation") String eventEstimation);

    //Заходи
    @GET("/api/v1/sport-events{my}")
    Observable<SportEvents> getEvents(@Path(value = "my", encoded = true) String url, @QueryMap Map<String, String> options);

    @GET("/api/v1/sport-events{my}")
    Observable<SportEvents> getEvents(@Header("Authorization") String token, @Path(value = "my", encoded = true) String url, @QueryMap Map<String, String> options);

    @GET("/api/v1/users/{id}/event-evaluation")
    Observable<SportEvents> getRaitingEvents(@Header("Authorization") String token, @Path(value = "id", encoded = true) String id);


    //Заходи деталі
    @GET("/api/v1/sport-events/{id}")
    Observable<ItemEvent> getEventDetails(@Header("Authorization") String token, @Path("id") String id);

    @GET("/api/v1/sport-events/{id}")
    Observable<ItemEvent> getEventDetails(@Path("id") String id);

    //Запис івента
    @GET("/api/v1/meetings/meeting_records.php")
    Observable<MeetingsModel> meetingsRequest(@Header("Authorization") String token, @Query("id") String id);

    //Заходи список користувачів
    @GET("/api/v1/sport-events/{id}/participants/{user}")
    Observable<UserParticipants> getEventUser(@Header("Authorization") String token, @Path("id") String id, @Path("user") String user);

    //Відправка коду
    @POST("/api/v1/users/email")
    @FormUrlEncoded
    Single<Default> sendOfCodeEmail(@Field("email") String email);

    //Відправка коду
    @POST("/api/v1/users/restore-password")
    @FormUrlEncoded
    Observable<ResponseBody> restorePassword(@Field("email") String email, @Field("code") String code, @Field("password") String password);

    //Получення відео
    @GET("/api/v1/videos")
    Observable<Video> getVideo(@Query("sort[priority]") String priority, @Query("filters[statusId]") String statusId, @Query("filters[categoryId]") String categoryId, @Query("filters[exerciseDifficultyLevelId]") String exerciseDifficultyLevelId);

    @GET("/api/v1/user-videos")
    Observable<Video> getVideoUsers(@Query("sort[priority]") String priority, @Query("filters[statusId]") String statusId, @Query("filters[categoryId]") String categoryId, @Query("filters[exerciseDifficultyLevelId]") String exerciseDifficultyLevelId);

    //Получення відео з списка
    @GET("/api/v1/videos/{id}/random")
    Observable<Video> getVideoRandom(@Path("id") String id, @Query("sort[priority]") String priority, @Query("filters[statusId]") String statusId, @Query("filters[categoryId]") String categoryId, @Query("filters[exerciseDifficultyLevelId]") String exerciseDifficultyLevelId);


    //Получення даних
    @GET("/api/v1/dictionaries")
    Observable<Dictionaries> getDictionaries();

    //Получення даних мапи
    @GET("/api/v1/sportsgrounds/all?filters[statusId]=036735ba-f634-469b-ac30-77a164e3a918&sort[distanceToPoint]=asc")
    Observable<Sportsgrounds> getSportsgrounds(@Query("limit") String limit, @Query("filters[radius]") String radius, @Query("filters[longitude]") String longitude, @Query("filters[latitude]") String latitude);

    @GET("/city.php")
    Observable<City> searchCity(@Query("q") String q);

    @GET("/reverse?format=json&accept-language=ua")
    Observable<Location> locationRequest(@QueryMap(encoded = true) Map<String, String> options);


    //Получення даних мапи
    @GET("/api/v1/sportsgrounds/{id}")
    Observable<ItemSportsground> getSportsground(@Path("id") String id);

    //Получення даних мапи
    @PUT("/api/v1/users/set-push-token")
    @FormUrlEncoded
    Observable<ResponseBody> setPush(@Header("Authorization") String token, @Field("pushToken") String pushToken);

    //Получення даних користувача
    @GET("/api/v1/users/{id}")
    Observable<User> getUser(@Header("Authorization") String token, @Path("id") String id);

    //Получення даних користувача
    @PUT("/api/v1/users/{id}")
    Observable<ResponseBody> updateUser(@Header("Authorization") String token, @Path("id") String id, @Body UserUpdate user);

    @FormUrlEncoded
    @POST("/api/v1/users/phone")
    Observable<ResponseBody> sendSmsRequest(@Header("Authorization") String token, @Field("phone") String pushToke);

    @POST("/api/v1/users/{user}/phone-verify")
    @FormUrlEncoded
    Observable<ResponseBody> verifyUserPhoneRequest(@Header("Authorization") String token, @Path("user") String user, @Field("id") String id, @Field("phone") String phone, @Field("code") String code);

    @FormUrlEncoded
    @POST("/api/v1/users/{user}/change-email")
    Observable<ResponseBody> verifyUserEmailRequest(@Header("Authorization") String token, @Path("user") String user, @Field("id") String id, @Field("email") String email, @Field("code") String code);


    @DELETE("/api/v1/users/{id}")
    Observable<User> removeUser(@Header("Authorization") String token, @Path("id") String id);

    //Вихід
    @POST("/api/v1/auth/logout")
    Observable<ResponseBody> logout(@Header("Authorization") String token);

    //Всі відео
    @GET("/api/v1/user-videos/my?offset=0&limit=30&sort[priority]=desc&sort[updatedAt]=asc")
    Observable<UserVideo> getUserVideos(@Header("Authorization") String token);

    //Відео
    @GET("/api/v1/user-videos/{id}")
    Observable<UserVideoItem> getUserVideo(@Header("Authorization") String token, @Path("id") String id);

    //Створити
    @POST("/api/v1/user-videos")
    Observable<UserVideoItem> createUserVideo(@Header("Authorization") String token);

    //Оновити відео
    @PUT("/api/v1/user-videos/{id}")
    @Headers("Content-type: application/json")
    Observable<UserVideoItem> updateUserVideo(@Header("Authorization") String token, @Path("id") String id, @Body UserVideoItem saveBill);

    //Відправити на модерацію
    @PUT("/api/v1/user-videos/{id}/send")
    Observable<ResponseBody> sendUserVideo(@Header("Authorization") String token, @Path("id") String id);

    @DELETE("/api/v1/user-videos/{id}")
    Observable<ResponseBody> deleteUserVideo(@Header("Authorization") String token, @Path("id") String id);

    //Get user notifications
    @GET("/api/v1/notifications?offset=0&limit=20")
    Observable<Notifications> getNotifications(@Header("Authorization") String token);

    //Get user notifications
    @GET("/api/v1/clubs/creator?offset=0&limit=50")
    Observable<Clubs> getClubsCreator(@Header("Authorization") String token);

    //Get user notifications
    @GET("/api/v1/clubs/participant?offset=0&limit=50")
    Observable<Clubs> getClubsParticipant(@Header("Authorization") String token);

    //Get clubs
    @GET("/api/v1/clubs?offset=0")
    Observable<Clubs> getClubsAll(@Header("Authorization") String token, @Query("limit") String limit);

    @GET("/api/v1/clubs?offset=0&limit=30")
    Observable<Clubs> getSearchClubsAll(@Header("Authorization") String token, @Query("filters[all]") String filter);

    //Get clubs
    @GET("/api/v1/clubs/my?offset=0&limit=40")
    Observable<ClubsUserIsMemberModel> getClubs(@Header("Authorization") String token, @Query("userId") String id);

    //Get club details
    @GET("/api/v1/clubs/{id}")
    Observable<ItemClub> getClubsDetails(@Header("Authorization") String token, @Path("id") String id);

    //Create club details
    @POST("/api/v1/clubs/{id}/news")
    Observable<Default> setClubsNews(@Header("Authorization") String token, @Path("id") String id, @Body NewsClubs news);

    //Admin club user details
    @GET("/api/v1/clubs/{id}/participants/{user}")
    Observable<UserParticipants> getClubsUser(@Header("Authorization") String token, @Path("id") String id, @Path("user") String user);

    //Admin club user details
    @GET("/api/v1/clubs/{id}/participants/applying")
    Observable<UserParticipants> getClubsUserApplying(@Header("Authorization") String token, @Path("id") String id);

    @GET("/api/v1/sport-events/{id}/participants/applying")
    Observable<UserParticipants> getEventUserApplying(@Header("Authorization") String token, @Path("id") String id);

    //Apply User
    @POST("/api/v1/clubs/{id}/apply-user")
    Observable<ResponseBody> getApplyUser(@Header("Authorization") String token, @Path("id") String id);

    //Approve User
    @POST("/api/v1/clubs/{id}/approve-user")
    @FormUrlEncoded
    Observable<ResponseBody> getApproveUser(@Header("Authorization") String token, @Path("id") String id, @Field("userId") String userId);

    @POST("/api/v1/sport-events/{id}/{type}")
    @FormUrlEncoded
    Observable<ResponseBody> eventApplyingRequest(@Header("Authorization") String token, @Field("userId") String userId, @Path("id") String id, @Path("type") String type);

    @POST("/api/v1/sport-events/{id}/{type}")
    Observable<ResponseBody> eventPostRequest(@Header("Authorization") String token, @Path("id") String id, @Path("type") String type);

    @POST("/api/v1/clubs/{id}/{type}")
    @FormUrlEncoded
    Observable<ResponseBody> clubsApplyingRequest(@Header("Authorization") String token, @Field("userId") String userId, @Path("id") String id, @Path("type") String type);

    //Reject User
    @POST("/api/v1/clubs/{id}/reject-user")
    @FormUrlEncoded
    Observable<ResponseBody> rejectUser(@Header("Authorization") String token, @Path("id") String id, @Field("userId") String userId);

    //Remove User
    @POST("/api/v1/clubs/{id}/remove-user")
    @FormUrlEncoded
    Observable<ResponseBody> removeUser(@Header("Authorization") String token, @Path("id") String id, @Field("userId") String userId);

    //Get Support List
    @GET("/api/v1/support-tickets?offset=0&limit=30&sort[lastStatusChange]=desc")
    Observable<Support> getSupportList(@Header("Authorization") String token);

    //Створиння питання
    @POST("/api/v1/support-tickets")
    Observable<SupportItem> createSupport(@Header("Authorization") String token);

    //Оновлення даних питання
    @PUT("/api/v1/support-tickets/{id}")
    Observable<ResponseBody> updateSupport(@Header("Authorization") String token, @Path("id") String id, @Body SupportItem user);

    //Get Support details
    @GET("/api/v1/support-tickets/{id}")
    Observable<SupportItem> getSupportDetails(@Header("Authorization") String token, @Path("id") String id);

    //Post Support details
    @POST("/api/v1/support-tickets/{id}/send")
    Observable<SupportItem> sendMessage(@Header("Authorization") String token, @Path("id") String id);

    @POST("/api/v1/support-tickets/{id}/messages")
    @FormUrlEncoded
    Observable<SupportItem> sendMessage(@Header("Authorization") String token, @Path("id") String url, @Field("id") String id, @Field("text") String text);

    @GET("/api/v1/workouts/{url}")
    Observable<SportEvents> myevents(@Header("Authorization") String token, @Path(value = "url", encoded = true) String url, @QueryMap(encoded = true) Map<String, String> options);

    //Workouts Sport
    @GET("/api/v1/workouts/{url}")
    Observable<WorkoutModel> workout(@Header("Authorization") String token, @Path(value = "url", encoded = true) String url, @QueryMap(encoded = true) Map<String, String> options);

    //Workouts Sport
    @PUT("/api/v1/workouts/{url}")
    Observable<WorkoutModel> putWrkout(@Header("Authorization") String token, @Path(value = "url", encoded = true) String url, @QueryMap(encoded = true) Map<String, String> options);

    @POST("/api/v1/workouts/{url}")
    @FormUrlEncoded
    Observable<WorkoutModel> addWorkoutNote(@Header("Authorization") String token, @Path(value = "url", encoded = true) String url, @Field("title") String title);

    //Workouts Sport
    @GET("/api/v1/workouts/{url}")
    Observable<WorkoutItem> workout(@Header("Authorization") String token, @Path(value = "url") String url);

    @POST("/api/v1/workouts/{type}")
    @FormUrlEncoded
    Observable<ResponseBody> setPermissionsRequest(@Header("Authorization") String token, @Path(value = "type") String url, @Field("clubId") String clubId);

    //Workouts Sport
    @POST("/api/v1/workouts")
    Observable<ResponseBody> workoutAddRequest(@Header("Authorization") String token, @Body PlanModel request);

    @PUT("/api/v1/workouts/{id}")
    Observable<WorkoutModel> workoutUpdateRequest(@Header("Authorization") String token, @Body PlanModel request);

    @DELETE("/api/v1/workouts/{id}")
    Observable<WorkoutModel> workoutRemoveRequest(@Header("Authorization") String token, @Path(value = "id") String url);

    //Post Sport details
    @POST("/api/v1/sport-events/{event}/points/{point}/pass")
    Observable<ResponseBody> pintRequest(@Header("Authorization") String token, @Path("event") String event, @Path("point") String point);

    //Post Sport details
    @GET("/api/v1/qr-code-club/{url}")
    Observable<QrCodeModel> activateClubQrCodeRequest(@Header("Authorization") String token, @Path("url") String url);

    @POST("/api/v1/statistics/mobile")
    @FormUrlEncoded
    Observable<ScanerResultModel> activateScanerCodeRequest(@Field("url") String url);

    //Post Sport details
    @GET("/api/v1/qr-code-point/{url}")
    Observable<QrCodeModel> activatePointQrCodeRequest(@Header("Authorization") String token, @Path("url") String url);

    @GET("/api/v1/sport-events/{id}")
    Observable<QrCodeModel> createQrCodePointRequest(@Header("Authorization") String token, @Path("id") String id);

    @POST("/api/v1/sport-events/{url}")
    @FormUrlEncoded
    Observable<ResponseBody> jointEvent(@Header("Authorization") String token, @Path("url") String url, @Field("userId") String userId, @Field("id") String id);

    @POST("/api/v1/qr-code-club/to-join-club")
    @FormUrlEncoded
    Observable<QrCodeModel> createQrCodeClubRequest(@Header("Authorization") String token, @Field("title") String title, @Field("clubId") String clubId, @Field("endDateOfUse") String endDateOfUse);

    //Upload file
    @POST("/api/v1/uploads")
    @Multipart
    Observable<Default> updateFile(@Header("Authorization") String token,
                             @Part("fileName") String fileName,
                             @Part("size") int size,
                             @Part("chunkIndex") int chunkIndex,
                             @Part("totalChunk") int totalChunk,
                             @Part("uploadId") String uploadId,
                             @Part("uploadType") RequestBody uploadType,
                             @Part MultipartBody.Part file);
}
