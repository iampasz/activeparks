package com.app.activeparks.ui.web;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.technodreams.activeparks.R;

public class WebActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_web);

            WebView webView = findViewById(R.id.web_view);

            ImageView closed = findViewById(R.id.closed);

            TextView titleText = findViewById(R.id.title);

            titleText.setText(getIntent().getStringExtra("TITLE"));

            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36");

            webView.loadUrl(getIntent().getStringExtra("WEB_URL"));

            closed.setOnClickListener(v-> {
                finish();
            });
        }
}