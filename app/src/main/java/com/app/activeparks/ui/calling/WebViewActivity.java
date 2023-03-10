package com.app.activeparks.ui.calling;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.technodreams.activeparks.R;

public class WebViewActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_calling);

            WebView webView = findViewById(R.id.web_view);

            ImageView closed = findViewById(R.id.closed);


            webView.loadUrl(getIntent().getStringExtra("WEB_URL"));

            closed.setOnClickListener(v-> {
                finish();
            });
        }
}