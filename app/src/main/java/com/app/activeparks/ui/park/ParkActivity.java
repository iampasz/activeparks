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

import com.app.activeparks.data.model.clubs.ItemClub;
import com.app.activeparks.ui.adapter.PhotosAdaper;
import com.app.activeparks.ui.clubs.ClubActivity;
import com.app.activeparks.ui.clubs.adapter.ClubsAdaper;
import com.app.activeparks.ui.home.adapter.HomeAdaper;
import com.app.activeparks.util.MapsViewControler;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.technodreams.activeparks.R;

import org.osmdroid.views.MapView;

public class ParkActivity extends AppCompatActivity {

    private ParkViewModel mViewModel;

    private RecyclerView mParkEvent;

    private TabLayout tabLayout;
    private ViewPager2 photosView;
    private ImageView mImageView, mPhotoCordenator;
    private TextView mTitle, mCoordinator, mAddressCity, mLighting, mFacebook, mTechnicalCondition;
    private TextView mNameCordenator, mEmailCordenator, mPhoneCordenator;
    private LinearLayout mItemCordenator;
    public MapView mapView;
    public MapsViewControler mapsViewControler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_park);
        mViewModel = new ViewModelProvider(this).get(ParkViewModel.class);

        mapView = findViewById(R.id.mapview);

        mParkEvent = findViewById(R.id.park_event);

        mapsViewControler = new MapsViewControler(mapView, this);

        mViewModel.getPark(getIntent().getStringExtra("id"));

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
        mLighting = findViewById(R.id.text_lighting);
        mFacebook = findViewById(R.id.text_facebook);
        mTechnicalCondition = findViewById(R.id.text_technical_condition);

        mNameCordenator = findViewById(R.id.name_cordenator);
        mEmailCordenator = findViewById(R.id.email_cordenator);
        mPhoneCordenator = findViewById(R.id.phone_cordenator);

        mItemCordenator = findViewById(R.id.item_cordenator);



        mViewModel.getParkDetails().observe(this, park -> {
            try {
            mTitle.setText(park.getTitle());
            Spanned textSpan  =  Html.fromHtml("<u>" + park.getLocation().get(0) + " " + park.getLocation().get(1) + "</u>");
            mCoordinator.setText(textSpan);

            mCoordinator.setOnClickListener(v ->{
                String uri = "https://google.com/maps/search/?api=1&query=" + park.getLocation().get(0) + "," + park.getLocation().get(1);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                //intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            });

            mAddressCity.setText(park.getCity() + " " +park.getStreet());
            mLighting.setText(park.getHasLighting() > 0 ? "Присутнє" : "Відсутнє");
            mTechnicalCondition.setText(park.getOnReconstruction() == false ? "Новий" : "Реконсту");
            mFacebook.setText(park.getFacebookUrl() != null ? park.getFacebookUrl() : "Відсутнє");
            
            photosView.setAdapter(new PhotosAdaper(this, park.getPhotos()));

            new TabLayoutMediator(tabLayout, photosView, (tab, position) -> {
            }).attach();

            mapsViewControler.setMarker(park.getLocation().get(0), park.getLocation().get(1));

            Glide.with(this).load(park.getPhoto()).into(mImageView);

            if (park.getCoordinators() != null) {
                Glide.with(this).load(park.getCoordinators().get(0).getPhoto()).into(mPhotoCordenator);
                mItemCordenator.setVisibility(View.VISIBLE);

                mNameCordenator.setText(park.getCoordinators().get(0).getFirstName() + " " + park.getCoordinators().get(0).getLastName());
                mEmailCordenator.setText("Email: " + park.getCoordinators().get(0).getEmail());
                mPhoneCordenator.setText("Телефон: " + park.getCoordinators().get(0).getPhone());
            }

                if (park.getSportEvents().size() > 0) {
                    findViewById(R.id.text_event).setVisibility(View.GONE);
//                    mParkEvent.setAdapter(new ClubsAdaper(this, park.getSportpark()).setOnClubsListener(new ClubsAdaper.ClubsListener() {
//                        @Override
//                        public void onInfo(ItemClub itemClub) {
//                            startActivity(new Intent(getApplicationContext(), ClubActivity.class).putExtra("id", itemClub.getId()));
//                        }
//                    }));
                }


            }catch (Exception e){}
        });
    }

}