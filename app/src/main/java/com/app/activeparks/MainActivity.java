package com.app.activeparks;


import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.app.activeparks.data.repository.Repository;
import com.app.activeparks.data.storage.Preferences;
import com.app.activeparks.ui.maps.MapsFragment;
import com.app.activeparks.ui.profile.EditProfileActivity;
import com.app.activeparks.ui.profile.FragmentProfileOld;
import com.app.activeparks.ui.user.UserFragment;
import com.app.activeparks.util.Dictionarie;
import com.app.activeparks.util.FragmentInteface;
import com.app.activeparks.util.cropper.CropImage;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.technodreams.activeparks.R;
import com.technodreams.activeparks.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements FragmentInteface {

    private ActivityMainBinding binding;
    private AppUpdateManager appUpdateManager;

    private NavController navControllerHome;
    private NavController navControllerMain;
    private NavController navControllerProfile;
    private Preferences preferences;
    private final int PICK_IMAGE_REQUEST = 1;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        appUpdateManager = AppUpdateManagerFactory.create(this);

        navControllerHome = Navigation.findNavController(this, R.id.navFragmentsHomeUser);
        NavigationUI.setupWithNavController(binding.iHomeUser.navHomeUser, navControllerHome);

        navControllerProfile = Navigation.findNavController(this, R.id.navFragmentsUserProfile);
        NavigationUI.setupWithNavController(binding.iUserProfile.navProfileUser, navControllerProfile);

        navControllerMain = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navControllerMain);

        new Dictionarie().init(this);
        updatePushToken();

        Locale locale = new Locale("uk-rUA");
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, null);
        preferences = new Preferences(this);


        binding.navView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home -> {
                    navControllerMain.navigate(R.id.navigation_home);
                    setVisibleHome(VISIBLE, GONE, GONE);
                }
                case R.id.navigation_maps -> {
                    navControllerMain.navigate(R.id.navigation_maps);
                    setVisibleHome(GONE, GONE, VISIBLE);
                }
                case R.id.navigation_scaner -> {
                    navControllerMain.navigate(R.id.navigation_scaner);
                    setVisibleHome(GONE, GONE, VISIBLE);
                }
                case R.id.navigation_active -> {
                    if (preferences.getToken() == null || preferences.getToken().isEmpty()) {
                        navControllerMain.navigate(R.id.registration_user);
                    } else {
                        navControllerMain.navigate(R.id.navigation_active);
                    }
                    setVisibleHome(GONE, GONE, GONE);
                }
                case R.id.navigation_user -> {
                    if (preferences.getToken() == null || preferences.getToken().isEmpty()) {
                        navControllerMain.navigate(R.id.registration_user);
                        setVisibleHome(GONE, GONE, GONE);
                    } else {
                        navControllerMain.navigate(R.id.navigation_user);
                        setVisibleHome(GONE, VISIBLE, GONE);
                    }
                }
            }

            return false;
        });

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, this, 1);
                } catch (IntentSender.SendIntentException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        if (preferences.getToken() != null && !preferences.getToken().isEmpty()) {
            binding.iHomeUser.tvUserName.setText(preferences.getUserName());
            binding.iHomeUser.ivUser.setOnClickListener(v -> openGallery());
        } else {
            binding.iHomeUser.tvUserTitle.setText("Ласкаво просимо");
            binding.iHomeUser.ivUser.setVisibility(GONE);
            binding.iHomeUser.inNotification.setVisibility(GONE);
        }

        binding.iUserProfile.btnMoreInfo.setOnClickListener(v -> {
            setVisibleHome(GONE, GONE, VISIBLE);
        });
    }


    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    private void setVisibleHome(int visibleHome, int visibleProfile, int visibleMain) {
        binding.iHomeUser.getRoot().setVisibility(visibleHome);
        binding.iUserProfile.getRoot().setVisibility(visibleProfile);
        findViewById(R.id.nav_host_fragment_activity_main).setVisibility(visibleMain);
    }

    public NavController getNavControllerMain() {
        return navControllerMain;
    }

    public NavController getNavControllerHome() {
        return navControllerHome;
    }

    public NavController getNavControllerProfile() {
        return navControllerProfile;
    }

    public void openFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.navFragment, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getFragments().size() > 2) {
            if (getSupportFragmentManager()
                    .getFragments()
                    .get(getSupportFragmentManager().getFragments().size() - 1) instanceof FragmentProfileOld) {
                setVisibleHome(GONE, VISIBLE, GONE);
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .remove(
                                getSupportFragmentManager()
                                        .getFragments()
                                        .get(getSupportFragmentManager().getFragments().size() - 1))
                        .commit();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigation(getIntent().getIntExtra("ID", R.id.navigation_home));
        getIntent().removeExtra("ID");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3 || requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            EditProfileActivity fragment = (EditProfileActivity) getSupportFragmentManager().findFragmentByTag("MY_FRAGMENT_TAG");
            assert fragment != null;
            fragment.onActivityResult(requestCode, resultCode, data);
        }

        if (data == null) {
            return;
        }
        try {
            int number = data.getIntExtra("ID_MAP", 0);
            if (number >= 1 && number <= 6) {
                show(new MapsFragment());
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                binding.iHomeUser.ivUser.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void show(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, fragment)
                .commit();
    }

    @Override
    public void navigation(int id) {
        binding.navView.setSelectedItemId(id);
    }

    @Override
    public void message(String msg) {
        if (msg != null) {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }
    }

    public void updatePushToken() {
        Preferences preferences = new Preferences(this);
        if (preferences.getServer()) {
            //message("Тестовий сервер включений");
        }
        if (preferences.getPushToken() != null) {
            new Repository(preferences).setPush(preferences.getPushToken());
        }
        if (preferences.getToken() != null && preferences.getToken().length() > 0) {
            reviewManager();
        }
    }

    public void reviewManager() {
        ReviewManager manager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();
                Task<Void> flow = manager.launchReviewFlow(this, reviewInfo);
                flow.addOnCompleteListener(tasks -> {
                });
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}