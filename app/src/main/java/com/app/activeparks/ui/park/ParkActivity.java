package com.app.activeparks.ui.park;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.app.activeparks.data.model.parks.ParksItem;
import com.app.activeparks.data.model.sportevents.ItemEvent;
import com.app.activeparks.data.model.sportsgrounds.ItemSportsground;
import com.app.activeparks.ui.adapter.PhotosAdaper;
import com.app.activeparks.ui.event.activity.EventActivity;
import com.app.activeparks.ui.event.adapter.EventsListAdaper;
import com.app.activeparks.ui.park.adapter.ParkListAdaper;
import com.app.activeparks.util.MapsViewController;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.technodreams.activeparks.R;

import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;

public class ParkActivity extends AppCompatActivity {

    private ParkViewModel viewModel;

    private RecyclerView list, parkEvent;

    private TabLayout tabLayout;
    private ViewPager2 photosView;
    private ImageView mImageView, mPhotoCordenator;
    private TextView mTitle, mCoordinator, mAddressCity, eventStatus;
    private TextView mNameCordenator, mEmailCordenator, mPhoneCordenator;
    private LinearLayout mItemCordenator;
    public MapView mapView;
    public MapsViewController mapsViewController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park);
        viewModel = new ViewModelProvider(this, new ParkModelFactory(this)).get(ParkViewModel.class);

        mapView = findViewById(R.id.mapview);

        list = findViewById(R.id.list);

        parkEvent = findViewById(R.id.park_event);

        mapsViewController = new MapsViewController(mapView, this);

        viewModel.getPark(getIntent().getStringExtra("id"));

        findViewById(R.id.closed).setOnClickListener((View v) -> {
            finish();
        });

        mImageView = findViewById(R.id.image_club);
        mPhotoCordenator = findViewById(R.id.photo_cordenator);

        tabLayout = findViewById(R.id.list_tab);
        photosView = findViewById(R.id.list_photo);
        photosView.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        mTitle = findViewById(R.id.title);
        mCoordinator = findViewById(R.id.text_description);
        mAddressCity = findViewById(R.id.text_address);
        eventStatus = findViewById(R.id.text_event);

        mNameCordenator = findViewById(R.id.name_cordenator);
        mEmailCordenator = findViewById(R.id.email_cordenator);
        mPhoneCordenator = findViewById(R.id.phone_cordenator);

        mItemCordenator = findViewById(R.id.item_cordenator);

        viewModel.getParkDetails().observe(this, park -> {
            try {
                mTitle.setText(park.getTitle());
                Spanned textSpan = Html.fromHtml("<u>" + park.getLocation().get(0) + " " + park.getLocation().get(1) + "</u>");
                mCoordinator.setText(textSpan);
                mCoordinator.setTextColor(getResources().getColor(R.color.color_park));

                mCoordinator.setOnClickListener(v -> {
                    if (park.getLocation() != null) {
                        String uri = "https://google.com/maps/search/?api=1&query=" + park.getLocation().get(0) + "," + park.getLocation().get(1);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(intent);
                    }
                });

                mAddressCity.setText(park.getCity() + " " + park.getStreet());

                listPark(park);

                photosView.setAdapter(new PhotosAdaper(this, park.getPhotos()));

                new TabLayoutMediator(tabLayout, photosView, (tab, position) -> {
                }).attach();

                mapsViewController.setMarker(park.getLocation().get(0), park.getLocation().get(1));

                Glide.with(this).load(park.getPhoto()).error(R.drawable.ic_prew).into(mImageView);

                if (park.getCoordinators() != null) {
                    Glide.with(this).load(park.getCoordinators().get(0).getPhoto()).error(R.drawable.ic_prew).into(mPhotoCordenator);
                    mItemCordenator.setVisibility(View.VISIBLE);

                    mNameCordenator.setText(park.getCoordinators().get(0).getFirstName() + " " + park.getCoordinators().get(0).getLastName());
                    mEmailCordenator.setText("Email: " + park.getCoordinators().get(0).getEmail());
                    mPhoneCordenator.setText("Телефон: " + park.getCoordinators().get(0).getPhone());
                }

                if (park.getSportEvents().size() > 0) {
                    eventStatus.setVisibility(View.GONE);
                    parkEvent.setVisibility(View.VISIBLE);
                    parkEvent.setAdapter(new EventsListAdaper(this, park.getSportEvents()).setOnEventListener(new EventsListAdaper.EventsListener() {
                        @Override
                        public void onInfo(ItemEvent itemClub) {
                            startActivity(new Intent(getBaseContext(), EventActivity.class).putExtra("id", itemClub.getId()));
                        }

                        @Override
                        public void onOpenMaps(double lat, double lon) {
                            String uri = "https://www.google.com/maps/search/?api=1&query=" + lat + "," + lon;
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            startActivity(intent);
                        }
                    }));
                }

            } catch (Exception e) {
            }
        });
    }

    void listPark(ItemSportsground park){
        List<ParksItem> item = new ArrayList<>();

        if (park.getCapacityId() != null && !park.getCapacityId().isEmpty()){
            item.add(new ParksItem("Місткість", viewModel.capacity(park.getCapacityId())));
        }

        if (park.getHasLighting() != null){
            item.add(new ParksItem("Освітлення", park.getHasLighting() > 0 ? "Присутнє" : "Відсутнє"));
        }

        if (park.getOnReconstruction() != null){
            item.add(new ParksItem("Технічний стан", park.getOnReconstruction() == false ? "Відміний" : "Реконсту"));
        }

        if (park.getAccessTypeId() != null && !park.getAccessTypeId().isEmpty()){
            item.add(new ParksItem("Тип доступу", viewModel.accessTypeId(park.getAccessTypeId())));
        }

        if (park.getFacebookUrl() != null && park.getFacebookUrl().length() > 0){
            item.add(new ParksItem("Посилання на facebook", park.getFacebookUrl()));
        }

        if (park.getTypeId() != null && !park.getTypeId().isEmpty()){
            item.add(new ParksItem("Вид майданчику", viewModel.sportsgroundType(park.getTypeId())));
        }

        if (park.getOwnershipTypeId() != null && !park.getOwnershipTypeId().isEmpty()){
            item.add(new ParksItem("Форма власності", viewModel.ownershipType(park.getOwnershipTypeId())));
        }

        if (park.getWorkHours() != null && !park.getWorkHours().isEmpty()){
            item.add(new ParksItem("Режим роботи", park.getWorkHours()));
        }

        list.setAdapter(new ParkListAdaper(this, item).setListener(new ParkListAdaper.ParkListListener() {
            @Override
            public void onUrl(String url) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        }));
    }

}