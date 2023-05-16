package com.app.activeparks;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.app.activeparks.data.NetworkModule;
import com.app.activeparks.data.storage.Preferences;
import com.app.activeparks.repository.Repository;
import com.app.activeparks.ui.maps.MapsFragment;
import com.app.activeparks.ui.profile.EditProfileActivity;
import com.app.activeparks.util.Dictionarie;
import com.app.activeparks.util.FragmentInteface;
import com.app.activeparks.util.cropper.CropImage;
import com.technodreams.activeparks.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import com.technodreams.activeparks.databinding.ActivityMainBinding;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity implements FragmentInteface {

    private ActivityMainBinding binding;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavigationView = findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        new Dictionarie().init(this);
        updatePushToken();

        Locale locale = new Locale("uk-rUA");
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, null);

        //startActivity(new Intent(this, TestUpdate.class));
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
        bottomNavigationView.setSelectedItemId(id);
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
    }

}