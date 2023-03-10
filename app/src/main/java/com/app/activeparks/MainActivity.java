package com.app.activeparks;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.app.activeparks.data.storage.Preferences;
import com.app.activeparks.data.storage.bd.AppDatabase;
import com.app.activeparks.ui.maps.MapsFragment;
import com.app.activeparks.ui.profile.EditProfileActivity;
import com.app.activeparks.util.Dictionarie;
import com.app.activeparks.util.FragmentInteface;
import com.technodreams.activeparks.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import com.technodreams.activeparks.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity implements FragmentInteface {

    private ActivityMainBinding binding;
    public BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavigationView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        binding.actionScaner.setOnClickListener(v -> {
            navigation(R.id.navigation_scaner);
        });

        new Dictionarie().init(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3) {
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
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}