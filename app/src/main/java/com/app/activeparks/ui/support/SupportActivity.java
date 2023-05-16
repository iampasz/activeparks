package com.app.activeparks.ui.support;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.news.ItemNews;
import com.app.activeparks.ui.support.adapter.SupportAdaper;
import com.technodreams.activeparks.R;

public class SupportActivity extends AppCompatActivity {

    private ItemNews itemNews;

    private SupportViewModel supportViewModel;

    public SupportActivity init(ItemNews itemNews) {
        this.itemNews = itemNews;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        supportViewModel =
                new ViewModelProvider(this, new SupportModelFactory(this)).get(SupportViewModel.class);

        RecyclerView listSupport = findViewById(R.id.list_support);

        findViewById(R.id.closed).setOnClickListener(v -> {
            finish();
        });

        findViewById(R.id.create_support_action).setOnClickListener(v -> {
            startActivity(new Intent(this, SupportAddActivity.class));
        });

        supportViewModel.getSupportList().observe(this, support -> {
            if (support.getItems().size() > 0) {
                findViewById(R.id.list_null).setVisibility(View.GONE);
            }
            listSupport.setAdapter(new SupportAdaper(this, support.getItems()).setOnCliclListener(id -> {
                startActivity(new Intent(this, SupportAddActivity.class).putExtra("id", id));
            }));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        supportViewModel.getSupport();
    }
}