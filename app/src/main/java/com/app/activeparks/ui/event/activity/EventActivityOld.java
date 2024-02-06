package com.app.activeparks.ui.event.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.activeparks.ui.event.interfaces.EventScannerListener;
import com.app.activeparks.ui.event.fragments.MeetingsFragment;
import com.app.activeparks.ui.event.util.EventModelFactory;
import com.app.activeparks.ui.event.viewmodel.EventViewModel;
import com.app.activeparks.ui.participants.ParticipantsFragmentOld;
import com.app.activeparks.ui.routepoint.RoutePointFragment;
import com.app.activeparks.util.ButtonSelect;
import com.app.activeparks.util.LoadImage;
import com.app.activeparks.util.MapsViewController;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;

import org.osmdroid.views.MapView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class EventActivityOld extends AppCompatActivity implements EventScannerListener, Html.ImageGetter, SwipeRefreshLayout.OnRefreshListener {

    private EventViewModel viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView mImageView;
    private MaterialRatingBar ratingBar;
    private ButtonSelect mDescriptionAction, mLocationAction, mRecordsAction, mPeopleAction, conferenceAction, startPointAction, mJoinAction;
    private TextView mTitle, mAddress, mStartEvent, mEndEvent, mDescription, mClubName, mEventStatus, mDay, mHour, mMinutes, mSeconds;
    public MapView mapView;
    private View statusView;
    private LinearLayout layoutTimer;
    public MapsViewController mapsViewController;

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables", "ObsoleteSdkInt", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_old);

        viewModel = new ViewModelProvider(this, new EventModelFactory(this)).get(EventViewModel.class);

        viewModel.updateEventData(getIntent().getStringExtra("id"));

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(this);

        mImageView = findViewById(R.id.image_club);
        mTitle = findViewById(R.id.title);
        mAddress = findViewById(R.id.text_address);
        mStartEvent = findViewById(R.id.text_start_event);
        mEndEvent = findViewById(R.id.text_end_event);
        mDescription = findViewById(R.id.text_description);
        mClubName = findViewById(R.id.text_club_name);
        mEventStatus = findViewById(R.id.status);
        conferenceAction = findViewById(R.id.conference_action);
        startPointAction = findViewById(R.id.start_point_action);
        statusView = findViewById(R.id.status_view);
        layoutTimer = findViewById(R.id.layout_timer);
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

        mapsViewController = new MapsViewController(mapView, this);

        viewModel.getEventDetails().observe(this, events -> {
            try {
                Glide.with(this).load(events.getImageUrl()).error(R.drawable.ic_prew).into(mImageView);
                mTitle.setText(events.getTitle());
                if (events.getSportsground() != null && events.getSportsground().getTitle() != null) {
                    mAddress.setText(events.getSportsground().getTitle());
                } else {
                    if (events.getRoutePoints() != null && events.getRoutePoints().size() > 0) {
                        double lat = viewModel.address.getLocation().get(0);
                        double lon = viewModel.address.getLocation().get(1);
                        viewModel.location(lat, lon);
                        mAddress.setOnClickListener(v -> {
                            String uri = "https://google.com/maps/search/?api=1&query=" + lat + "," + lon;
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            startActivity(intent);
                        });
                    } else {
                        findViewById(R.id.layout_address).setVisibility(View.GONE);
                    }
                }


                if (events.getEventEstimation() != null) {
                    ratingBar.setRating(events.getEventEstimation());
                }

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date date = format.parse(Objects.requireNonNull(events.getStartsAt()));
                    assert date != null;
                    mStartEvent.setText(new SimpleDateFormat("dd MMMM yyyy", new Locale("uk", "UA")).format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (events.getStartsAt() != null && events.getFinishesAt() != null) {
                    int startIndex = Math.min(events.getStartsAt().length(), events.getStartsAt().length() - 3);
                    String startsAt = events.getStartsAt().substring(11, startIndex);
                    int endIndex = Math.min(events.getFinishesAt().length(), events.getFinishesAt().length() - 3);
                    String finishesAt = events.getFinishesAt().substring(11, endIndex);
                    mEndEvent.setText("| " + startsAt + " - " + finishesAt);
                }

                mEventStatus.setText(viewModel.statusMapper(events.getHoldingStatusId()));

                mEventStatus.setVisibility(View.VISIBLE);
                statusView.setVisibility(View.VISIBLE);

                if (Objects.requireNonNull(events.getHoldingStatusId()).contains("tg2po97g-96r3-36hr-74ty-6tfgj1p8dzxq")) {
                    statusView.setBackground(getResources().getDrawable(R.drawable.button_color, null));
                }

                if (events.getFullDescription() != null) {
                    mDescriptionAction.setVisibility(View.VISIBLE);
                    String web = "<html><head><LINK href=\"https://ap.sportforall.gov.ua/images/index.css\" rel=\"stylesheet\"/></head><body>" + events.getFullDescription() + "</body></html>";
                    web = web.replace("<img ", "<br><img ");
                    mDescription.setMovementMethod(LinkMovementMethod.getInstance());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        HtmlCompat.fromHtml(web, HtmlCompat.FROM_HTML_MODE_LEGACY);
                    } else {
                        mDescription.setText(HtmlCompat.fromHtml(web, HtmlCompat.FROM_HTML_MODE_LEGACY));
                    }
                }
                if (events.getClub() != null) {
                    mClubName.setText(events.getClub().getName());
                } else {
                    mClubName.setVisibility(View.GONE);
                }

                if (Objects.requireNonNull(events.getTypeId()).contains("bd09f36f-835c-49e4-88b8-4f835c1602ac")) {
                    mLocationAction.setVisibility(View.VISIBLE);
                    findViewById(R.id.layout_location).setVisibility(View.VISIBLE);
                    mapsViewController.setMarker(viewModel.address.getLocation().get(0), viewModel.address.getLocation().get(1));

                    if (Objects.requireNonNull(events.getStatusId()).contains("032fd5ba-f634-469b-3c30-77a1gh63a918") && events.getHoldingStatusId().contains("tg2po97g-96r3-36hr-74ty-6tfgj1p8dzxq")) {
                        if (events.getEventUser() != null && events.getEventUser().getIsCoordinator()) {
                            startPointAction.setVisibility(View.VISIBLE);
                            startPointAction.setEnabled(true);
                            if (events.getRouteStartedAt() == null) {
                                startPointAction.setText("Розпочати захід");
                            } else {
                                startPointAction.setText("Зупинити захід");
                            }
                        }
                    } else {
                        startPointAction.setVisibility(View.GONE);
                    }

                } else if (events.getTypeId().contains("848e3121-4a2b-413d-8a8f-ebdd4ecf2840")) {

                    findViewById(R.id.layout_location).setVisibility(View.VISIBLE);
                    mapsViewController.setMarker(viewModel.address.getLocation().get(0), viewModel.address.getLocation().get(1));


                } else if (events.getTypeId().contains("e58e5c86-5ca7-412f-94f0-88effd1a45a8")) {


                    conferenceAction.setOnClickListener(v -> {
                        if (events.getConferenceLink() != null && events.getConferenceLink().length() > 0) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(events.getConferenceLink()));
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "Онлайн конференцій в процесі створення", Toast.LENGTH_SHORT).show();
                        }
                    });

                    if (events.getHoldingStatusId().contains("6h8ad9c0-fd34-8kr4-h8ft-43vdm3n7do3p")) {
                        layoutTimer.setVisibility(View.VISIBLE);
                        startTimer(events.getTimeZoneDifference());
                    } else if (events.getHoldingStatusId().contains("tg2po97g-96r3-36hr-74ty-6tfgj1p8dzxq")) {
                        mRecordsAction.setVisibility(View.GONE);
                        layoutTimer.setVisibility(View.GONE);
                        conferenceAction.setVisibility(View.VISIBLE);
                    } else if (events.getHoldingStatusId().contains("0q8a6xc0-1nb4-1pr4-h5at-4sw3m0l387yp")) {
                        mRecordsAction.setVisibility(View.VISIBLE);
                        conferenceAction.setVisibility(View.GONE);
                    }
                }

                if (events.getEventUser() != null) {
                    if (events.getEventUser().getIsApproved()) {
                        mJoinAction.setText("Покинути захід");
                        if (events.getHoldingStatusId().contains("0q8a6xc0-1nb4-1pr4-h5at-4sw3m0l387yp")) {
                            findViewById(R.id.layout_rating).setVisibility(View.VISIBLE);
                        }
                    } else {
                        mJoinAction.setText("Відмінити запрошення");
                    }
                } else {
                    mJoinAction.setText("Приєднатись до заходу");
                }


                if (viewModel.getUserAuth() && !events.getHoldingStatusId().contains("0q8a6xc0-1nb4-1pr4-h5at-4sw3m0l387yp")) {
                    mJoinAction.setEnabled(true);
                    if (events.getEventUser() != null && events.getEventUser().getIsCoordinator()) {
                        mJoinAction.setVisibility(View.GONE);
                    } else {
                        mJoinAction.setVisibility(View.VISIBLE);
                    }
                }

            } catch (Exception ignored) {
            }

        });

        viewModel.getLocation().observe(this, location -> mAddress.setText(location));

        viewModel.getMeetingRecords().observe(this, meetings -> setFragment(new MeetingsFragment(meetings)));

        initClickListener();
    }

    void initClickListener() {
        findViewById(R.id.closed).setOnClickListener((View v) -> onBackPressed());

        mDescriptionAction.setOnClickListener(v -> {
            mDescription.setVisibility(View.VISIBLE);
            findViewById(R.id.event_fragment).setVisibility(View.GONE);
            replaceButton();
            mDescriptionAction.on();
        });

        mLocationAction.setOnClickListener(v -> showRoutePoint());

        startPointAction.setOnClickListener(v -> viewModel.startEvent());

        mRecordsAction.setOnClickListener(v -> {
            viewModel.meetingRecords();
            findViewById(R.id.event_fragment).setVisibility(View.GONE);
            replaceButton();
            mRecordsAction.on();
        });

        mPeopleAction.setOnClickListener(v -> {
            setFragment(new ParticipantsFragmentOld(viewModel.mId, false, true));
            replaceButton();
            mPeopleAction.on();
        });

        mJoinAction.setOnClickListener(v -> {
            viewModel.applyUser();
            mJoinAction.setEnabled(false);
        });

        ratingBar.setOnClickListener(v -> viewModel.setEstimation(ratingBar.getRating()));
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> viewModel.setEstimation(rating));

        findViewById(R.id.copy_action).setOnClickListener((View v) -> {
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Хочу тебе запросити до заходу \"" + mTitle.getText().toString() + "\"");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, "Хочу тебе запросити до заходу \"" + mTitle.getText().toString() + "\" \n\n https://ap.sportforall.gov.ua/fc-events/0/" + viewModel.mId + "\n\nПриєднуйся до нас! Та оздоровлюйся разом зі мною! \n\nРозроблено на завдання президента України для проекту “Активні парки”");
            startActivity(Intent.createChooser(intent, getString(R.string.app_name)));
        });
    }

    void replaceButton() {
        mDescriptionAction.off();
        mLocationAction.off();
        mRecordsAction.off();
        mPeopleAction.off();
    }

    void startTimer(long time) {

        if (time > 0) {
            conferenceAction.setVisibility(View.VISIBLE);
            layoutTimer.setVisibility(View.GONE);
            return;
        }
        new CountDownTimer(Math.abs(time), 1000) {

            @SuppressLint("DefaultLocale")
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
                viewModel.updateEventData(getIntent().getStringExtra("id"));
                cancel();
            }
        }.start();
    }

    void showRoutePoint() {
        RoutePointFragment dialog = new RoutePointFragment(viewModel.mId);
        dialog.show(getSupportFragmentManager(), "route_point_fragment");
    }

    void setFragment(Fragment fragment) {
        findViewById(R.id.event_fragment).setVisibility(View.VISIBLE);
        mDescription.setVisibility(View.GONE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.event_fragment, fragment)
                .commit();
    }


    @Override
    public void update() {
        viewModel.getUpdateEvent();
        viewModel.meetingRecords();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    @Override
    public Drawable getDrawable(String source) {
        LevelListDrawable d = new LevelListDrawable();
        @SuppressLint("UseCompatLoadingForDrawables") Drawable empty = getResources().getDrawable(R.drawable.logo_active_parks, null);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

        new LoadImage(mDescription.getWidth()).setListener(() -> {
            CharSequence t = mDescription.getText();
            mDescription.setText(t);
        }).execute(source, d);

        return d;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        viewModel.getUpdateEvent();
    }
}