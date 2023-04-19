package com.app.activeparks.ui.selectvideo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.activeparks.ui.event.EventActivity;
import com.app.activeparks.ui.video.VideoActivity;
import com.app.activeparks.util.FragmenSelectInteface;
import com.technodreams.activeparks.R;

public class SelectVideoActivity extends AppCompatActivity implements ContentVideoCallback {

    private SelectVideoViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        mViewModel =
                new ViewModelProvider(this, new SelectVideoModelFactory(this)).get(SelectVideoViewModel.class);

        RadioGroup radioGroup = findViewById(R.id.radioGroup);

        String code = getIntent().getStringExtra("code");
        if (code != null) {
            try {
                onSelectType(Integer.parseInt(code));
            }catch (Exception e){
                onSelectType(0);
            }
        } else {
            ContentTypeVideoFragment fragment = new ContentTypeVideoFragment();
            fragment.callback = this;
            seletcFrament(fragment);
        }

        mViewModel.showVideo().observe(this, modelSelectCategory -> {
            int radioButtonID = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = radioGroup.findViewById(radioButtonID);
            int idx = radioGroup.indexOfChild(radioButton);
            startActivity(new Intent(getBaseContext(), VideoActivity.class)
                    .putExtra("id", idx)
                    .putExtra("categoryId", modelSelectCategory.TYPE_CATEGORY_ID)
                    .putExtra("exerciseDifficultyLevelId", modelSelectCategory.TYPE_DIFFICULTY_LEVEL_ID)
            );
        });

        initClickListener();
    }

    void initClickListener(){
        findViewById(R.id.closed).setOnClickListener(v -> {
            onBackPressed();
        });

        findViewById(R.id.ic_bottom_one).setOnClickListener(v -> {
            mViewModel.rozmenka();
        });

        findViewById(R.id.ic_bottom_two).setOnClickListener(v -> {
            mViewModel.rozmenka();
        });

        findViewById(R.id.ic_bottom_three).setOnClickListener(v -> {
            mViewModel.likar();
        });
    }

    public void seletcFrament(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, fragment)
                .commit();
    }


    @Override
    public void onSelectType(int result) {
        if (result == -1) {
            ContentTypeVideoFragment fragment = new ContentTypeVideoFragment();
            fragment.callback = this;
            seletcFrament(fragment);
        }else{
            LevelVideoFragment fragment = new LevelVideoFragment(result == 3);
            fragment.callback = this;
            seletcFrament(fragment);
            mViewModel.selectActivePark(result);
        }
    }

    @Override
    public void onSelectLevel(int result) {
        mViewModel.type(result);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

}