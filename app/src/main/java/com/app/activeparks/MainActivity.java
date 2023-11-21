package com.app.activeparks;


import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.app.activeparks.data.storage.Preferences;
import com.app.activeparks.data.repository.Repository;
import com.app.activeparks.ui.maps.MapsFragment;
import com.app.activeparks.ui.profile.EditProfileActivity;
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

import java.util.Locale;


public class MainActivity extends AppCompatActivity implements FragmentInteface {

    private ActivityMainBinding binding;
//test
    private AppUpdateManager appUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        appUpdateManager =  AppUpdateManagerFactory.create(this);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        new Dictionarie().init(this);
        updatePushToken();

        Locale locale = new Locale("uk-rUA");
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, null);



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

        //startActivity(new Intent(this, TestUpdate.class));

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

    public void updatePushToken(){
        Preferences preferences = new Preferences(this);
        if (preferences.getServer() == true) {
            message("Тестовий сервер включений");
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
            } else {
            }
        });
    }


}