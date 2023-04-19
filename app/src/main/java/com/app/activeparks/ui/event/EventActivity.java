package com.app.activeparks.ui.event;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.ui.event.adapter.MeetingListAdaper;
import com.app.activeparks.ui.participants.ParticipantsFragment;
import com.app.activeparks.ui.scaner.ScanerBottomFragment;
import com.app.activeparks.ui.web.WebActivity;
import com.app.activeparks.util.ButtonSelect;
import com.app.activeparks.util.LoadImage;
import com.app.activeparks.util.MapsViewControler;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;

import org.osmdroid.views.MapView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class EventActivity extends AppCompatActivity implements LocationListener, EventScanerListener, Html.ImageGetter {

    private EventViewModel mViewModel;
    private ImageView mImageView;

    private MaterialRatingBar ratingBar;
    private ButtonSelect mDescriptionAction, mLocationAction, mRecordsAction, mPeopleAction, mStatusAction, mJoinAction;
    private TextView mTitle, mAddress, mPhone, mStartEvent, mEndEvent, mDescription, mClubName, mEventStatus, mDay, mHour, mMinutes, mSeconds;
    public MapView mapView;
    private LinearLayout mPanelPhone;
    public MapsViewControler mapsViewControler;

    private LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        overridePendingTransition(R.anim.start, R.anim.end);

        mViewModel = new ViewModelProvider(this, new EventModelFactory(this)).get(EventViewModel.class);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mViewModel.getEvent(getIntent().getStringExtra("id"));

        mImageView = findViewById(R.id.image_club);
        mTitle = findViewById(R.id.title);
        mAddress = findViewById(R.id.text_address);
        mPhone = findViewById(R.id.text_phone);
        mStartEvent = findViewById(R.id.text_start_event);
        mEndEvent = findViewById(R.id.text_end_event);
        mDescription = findViewById(R.id.text_description);
        mClubName = findViewById(R.id.text_club_name);
        mEventStatus = findViewById(R.id.status);
        mStatusAction = findViewById(R.id.status_action);

        mPanelPhone = findViewById(R.id.panel_phone);

        ratingBar = findViewById(R.id.ratingBar);

        mDay = findViewById(R.id.day_time);
        mHour = findViewById(R.id.hour_time);
        mMinutes = findViewById(R.id.minutes_time);
        mSeconds = findViewById(R.id.seconds_time);


        mDescriptionAction = findViewById(R.id.description_action);
        mLocationAction = findViewById(R.id.location_action);
        mRecordsAction = findViewById(R.id.records_action);
        mPeopleAction = findViewById(R.id.people_action);
        mJoinAction = findViewById(R.id.join_action);

        mapView = findViewById(R.id.mapview);

        mapsViewControler = new MapsViewControler(mapView, this);

        mViewModel.getEventDetails().observe(this, events -> {
            try {
                Glide.with(this).load(events.getImageUrl()).error(R.drawable.ic_prew).into(mImageView);
                mTitle.setText(events.getTitle());
                if (events.getSportsground() != null) {
                    mAddress.setText(events.getSportsground().getTitle());
                } else {
                    if (events.getRoutePoints() != null && events.getRoutePoints().size() > 0) {
                        double lat = events.getRoutePoints().get(0).getLocation().get(0);
                        double lon = events.getRoutePoints().get(0).getLocation().get(1);
                        mViewModel.location(lat, lon);
                        mAddress.setOnClickListener(v -> {
                            String uri = "https://google.com/maps/search/?api=1&query=" + lat + "," + lon;
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            startActivity(intent);
                        });
                    } else {
                        findViewById(R.id.layout_address).setVisibility(View.GONE);
                    }
                }

//                if (events.getCreatedBy().getPhone() != null) {
//                    mPanelPhone.setVisibility(View.VISIBLE);
//                    mPhone.setText(events.getCreatedBy().getPhone());
//                }

                if (events.getEventEstimation() != null){
                    ratingBar.setRating(events.getEventEstimation());
                }

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date date = format.parse(events.getStartsAt());
                    mStartEvent.setText(new SimpleDateFormat("dd MMMM yyyy", new Locale("uk", "UA")).format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                mEndEvent.setText(events.getStartsAt() != null ? events.getStartsAt().substring(11, events.getStartsAt().length() - 3) : "Недіомо");
                mEventStatus.setText(mViewModel.statusMapper(events.getHoldingStatusId()));


                if (events.getFullDescription() != null) {
                    mDescriptionAction.setVisibility(View.VISIBLE);
                    String web = "<html><head><LINK href=\"https://web.sportforall.gov.ua/images/index.css\" rel=\"stylesheet\"/></head><body>" + events.getFullDescription() + "</body></html>";
                    web = web.replace("<img ", "<br><img ");
                    mDescription.setMovementMethod(LinkMovementMethod.getInstance());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mDescription.setText(Html.fromHtml(web, this, null));
                    } else {
                        mDescription.setText(Html.fromHtml(web));
                    }
                }
                if (events.getClub() != null) {
                    mClubName.setText(events.getClub().getName());
                } else {
                    mClubName.setVisibility(View.GONE);
                }

                if (events.getTypeId().contains("bd09f36f-835c-49e4-88b8-4f835c1602ac")) {
                    mLocationAction.setVisibility(View.VISIBLE);
                    findViewById(R.id.layout_location).setVisibility(View.VISIBLE);
                    mapsViewControler.setMarker(events.getRoutePoints().get(0).getLocation().get(0), events.getRoutePoints().get(0).getLocation().get(1));

                } else if (events.getTypeId().contains("848e3121-4a2b-413d-8a8f-ebdd4ecf2840")) {

                    findViewById(R.id.layout_location).setVisibility(View.VISIBLE);
                    mapsViewControler.setMarker(events.getRoutePoints().get(0).getLocation().get(0), events.getRoutePoints().get(0).getLocation().get(1));

                } else if (events.getTypeId().contains("e58e5c86-5ca7-412f-94f0-88effd1a45a8")) {

                    if (events.getHoldingStatusId().contains("0q8a6xc0-1nb4-1pr4-h5at-4sw3m0l387yp")) {
                        if (events.getMeetingRecordsCount() != null && events.getMeetingRecordsCount() > 0) {
                            mRecordsAction.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (events.getConferenceLink() != null && events.getConferenceLink().length() > 0) {
                            mStatusAction.setVisibility(View.VISIBLE);
                            mStatusAction.setOnClickListener(v -> {
                                startActivity(new Intent(this, WebActivity.class)
                                        .putExtra("TITLE", "Трансляція")
                                        .putExtra("WEB_URL", events.getConferenceLink()));
                            });
                        } else {
                            findViewById(R.id.layout_timer).setVisibility(View.VISIBLE);
                            startTimer(events.getStartsAt());
                        }
                    }
                }

                if (events.getEventUser() != null) {
                    if (events.getEventUser().getIsApproved() == true) {
                        mJoinAction.setText("Покинути захід");
                        onStartLocationUpdates();
                        if (events.getHoldingStatusId().contains("0q8a6xc0-1nb4-1pr4-h5at-4sw3m0l387yp")) {
                            findViewById(R.id.layout_rating).setVisibility(View.VISIBLE);
                        }
                    } else {
                        mJoinAction.setText("Відмінити запрошення");
                    }
                } else {
                    mJoinAction.setText("Приєднатись до заходу");
                }

            } catch (Exception e) {
            }

        });

        if (mViewModel.getUserAuth()) {
            mJoinAction.setVisibility(View.VISIBLE);
        }

        mViewModel.getLocation().observe(this, location -> {
            mAddress.setText(location);
        });

        mViewModel.getMeetingRecords().observe(this, mettings -> {
            setFragment(new MeetingsFragment(mettings));
        });

        initClickListener();
    }

    void initClickListener() {
        findViewById(R.id.closed).setOnClickListener((View v) -> {
            onBackPressed();
        });

        mDescriptionAction.setOnClickListener(v -> {
            mDescription.setVisibility(View.VISIBLE);
            findViewById(R.id.event_fragment).setVisibility(View.GONE);
            replaceButton();
            mDescriptionAction.on();
        });

        mLocationAction.setOnClickListener(v -> {
            setFragment(new RoutePointFragment(mViewModel.routePoints, mViewModel.isCoordinator, this));
            replaceButton();
            mLocationAction.on();
        });

        mRecordsAction.setOnClickListener(v -> {
            mViewModel.meetingRecords();
            findViewById(R.id.event_fragment).setVisibility(View.GONE);
            replaceButton();
            mRecordsAction.on();
        });

        mPeopleAction.setOnClickListener(v -> {
            setFragment(new ParticipantsFragment(mViewModel.mId, false, true));
            replaceButton();
            mPeopleAction.on();
        });

        mJoinAction.setOnClickListener(v -> {
            mViewModel.applyUser();
        });

//        ratingBar.setOnClickListener(v -> {
//            mViewModel.setEstimation(ratingBar.getRating());
//        });
        ratingBar.setOnRatingBarChangeListener(new MaterialRatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mViewModel.setEstimation(rating);
            }
        });

        findViewById(R.id.copy_action).setOnClickListener((View v) -> {
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, mTitle.getText().toString());
            intent.putExtra(android.content.Intent.EXTRA_TEXT, mAddress.getText().toString());
            startActivity(Intent.createChooser(intent, getString(R.string.app_name)));
        });
    }

    void replaceButton() {
        mDescriptionAction.off();
        mLocationAction.off();
        mRecordsAction.off();
        mPeopleAction.off();
    }

    void startTimer(String data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(data);

            Calendar cal = Calendar.getInstance();

            long time = date.getTime() - cal.getTime().getTime();
            new CountDownTimer(time, 1000) {

                public void onTick(long millisUntilFinished) {

                    long Days = millisUntilFinished / (24 * 60 * 60 * 1000);
                    long Hours = millisUntilFinished / (60 * 60 * 1000) % 24;
                    long Minutes = millisUntilFinished / (60 * 1000) % 60;
                    long Seconds = millisUntilFinished / 1000 % 60;
                    //
                    mDay.setText(String.format("%02d", Days));
                    mHour.setText(String.format("%02d", Hours));
                    mMinutes.setText(String.format("%02d", Minutes));
                    mSeconds.setText(String.format("%02d", Seconds));
                }

                public void onFinish() {
                    mViewModel.getEvent(getIntent().getStringExtra("id"));
                    cancel();

                }
            }.start();
        } catch (ParseException pe) {
        }
    }

    void setFragment(Fragment fragment) {
        findViewById(R.id.event_fragment).setVisibility(View.VISIBLE);
        mDescription.setVisibility(View.GONE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.event_fragment, fragment)
                .commit();
    }

    public void onStartLocationUpdates() {

        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5, this);
    }


    @Override
    public void onLocationChanged(Location location) {
        // Update the location text view with the new location
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        mViewModel.routePoints(latitude, longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void update() {
        mViewModel.getUpdateEvent();
        mViewModel.meetingRecords();
        setFragment(new RoutePointFragment(mViewModel.routePoints, mViewModel.isCoordinator, this));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    @Override
    public Drawable getDrawable(String source) {
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty = getResources().getDrawable(R.drawable.logo_active_parks);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

        new LoadImage(mDescription.getWidth()).setListener(new LoadImage.Listener() {
            @Override
            public void onUpdate() {
                CharSequence t = mDescription.getText();
                mDescription.setText(t);
            }
        }).execute(source, d);

        return d;
    }
}