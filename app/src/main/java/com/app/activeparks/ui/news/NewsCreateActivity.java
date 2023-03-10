package com.app.activeparks.ui.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.technodreams.activeparks.R;

import java.io.File;

public class NewsCreateActivity extends AppCompatActivity {


        private NewsViewModel mViewModel;

        private EditText title, description;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_create_news);

            mViewModel =
                    new ViewModelProvider(this, new NewsModelFactory(this)).get(NewsViewModel.class);

            title = findViewById(R.id.title);
            description = findViewById(R.id.description);

            findViewById(R.id.closed).setOnClickListener(v-> {
                finish();
            });

            findViewById(R.id.create_news_action).setOnClickListener(v-> {
                mViewModel.setClubsCreatorNews(getIntent().getStringExtra("id"), title.getText().toString(), description.getText().toString());
            });

            findViewById(R.id.photo_action).setOnClickListener(v-> {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            });

        }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String uriString = data.getData().toString();
                    File file = new File(uriString);
                    mViewModel.updateFile(file);
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
            }
        }
    }
}