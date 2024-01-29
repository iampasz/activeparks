package com.app.activeparks.ui.support;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.ui.support.adapter.SupportMessageAdaper;
import com.technodreams.activeparks.R;

public class SupportAddActivity extends AppCompatActivity {

        private SupportViewModel viewModel;
        private EditText title, msg;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_support_add);
            viewModel =
                    new ViewModelProvider(this, new SupportModelFactory(this)).get(SupportViewModel.class);

            findViewById(R.id.closed).setOnClickListener(v-> finish());

            RecyclerView listChat = findViewById(R.id.list_chat);

            title = findViewById(R.id.title_text);
            msg = findViewById(R.id.msg_text);

            if (getIntent().getStringExtra("id") != null){
                viewModel.getSupportDetails(getIntent().getStringExtra("id"));
            }else {
                viewModel.createSupport();
                findViewById(R.id.list_null).setVisibility(View.GONE);
                findViewById(R.id.send_message).setVisibility(View.VISIBLE);
            }

            viewModel.getSupportItem().observe(this, item -> {
                if (item.getMessages().size() > 0) {
                    findViewById(R.id.list_null).setVisibility(View.GONE);
                }

                listChat.setAdapter(new SupportMessageAdaper(this, item.getMessages()));

                if (item.getStatusId().equals("28a1b923-1b1d-45b9-8839-6bgfd1afa365")) {
                    findViewById(R.id.send_message).setVisibility(View.VISIBLE);
                    title.setText(item.getTopic());
                    msg.setText(item.getText());
                }else if (item.getStatusId().equals("bha1bfd3-1b5d-45b9-8239-6bg7d4jfa323")) {
                    findViewById(R.id.send_message).setVisibility(View.VISIBLE);
                    findViewById(R.id.title_frame).setVisibility(View.GONE);
                }else{
                    findViewById(R.id.send_message).setVisibility(View.GONE);
                }
            });

            findViewById(R.id.create_support_action).setOnClickListener(v-> {
                if (viewModel.mSupportUpdate != null && viewModel.mSupportUpdate.getStatusId().equals("28a1b923-1b1d-45b9-8839-6bgfd1afa365")) {
                    viewModel.updateSupport(title.getText().toString(), msg.getText().toString());
                    finish();
                }else {
                    viewModel.sendSupportMessage(msg.getText().toString());
                    msg.setText("");
                }
            });
        }

}