package com.app.activeparks.ui.support;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.news.ItemNews;
import com.app.activeparks.ui.support.adapter.SupportMessageAdaper;
import com.technodreams.activeparks.R;

public class SupportAddActivity extends AppCompatActivity {

        private ItemNews itemNews;

        private SupportViewModel mViewModel;
        private EditText title, msg;
        private TextView listNull;

        public SupportAddActivity init(ItemNews itemNews){
            this.itemNews = itemNews;
            return this;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_support_add);
            mViewModel =
                    new ViewModelProvider(this, new SupportModelFactory(this)).get(SupportViewModel.class);

            findViewById(R.id.closed).setOnClickListener(v-> {
                finish();
            });

            RecyclerView listChat = findViewById(R.id.list_chat);

            title = findViewById(R.id.title_text);
            listNull = findViewById(R.id.list_null);
            msg = findViewById(R.id.msg_text);

            if (getIntent().getStringExtra("id") != null){
                mViewModel.getSupportDetails(getIntent().getStringExtra("id"));
            }else {
                mViewModel.createSupport();
                findViewById(R.id.list_null).setVisibility(View.GONE);
                findViewById(R.id.send_message).setVisibility(View.VISIBLE);
            }

            mViewModel.getSupportItem().observe(this, item -> {
                findViewById(R.id.list_null).setVisibility(View.GONE);
                if (item.getMessages().size() > 0) {
                    findViewById(R.id.list_message).setVisibility(View.VISIBLE);
                    listChat.setAdapter(new SupportMessageAdaper(this, item.getMessages()));
                }else{
                    findViewById(R.id.send_message).setVisibility(View.VISIBLE);
                    title.setText(item.getTopic());
                    msg.setText(item.getText());
                }
            });

            findViewById(R.id.create_support_action).setOnClickListener(v-> {
                mViewModel.updateSupport(title.getText().toString(), msg.getText().toString());
                finish();
            });
        }

}