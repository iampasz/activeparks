package com.app.activeparks.ui.news;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.app.activeparks.data.model.news.ItemNews;
import com.app.activeparks.ui.adapter.PhotosAdaper;
import com.app.activeparks.util.LoadImage;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class NewsActivity extends AppCompatActivity implements Html.ImageGetter {

    private RecyclerView galary;
    private NewsViewModel viewModel;
    private TextView html;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        overridePendingTransition(R.anim.start, R.anim.end);

        viewModel =
                new ViewModelProvider(this, new NewsModelFactory(this)).get(NewsViewModel.class);

        if (getIntent().getStringExtra("clubId") != null) {
            viewModel.getNewsDetails(getIntent().getStringExtra("clubId"), getIntent().getStringExtra("id"));
        }else{
            viewModel.getNewsDetails(getIntent().getStringExtra("id"));
        }

        ImageView closed = findViewById(R.id.closed);

        TextView titleText = findViewById(R.id.title);

        TextView createText = findViewById(R.id.create);

        html = findViewById(R.id.html);

        ImageView imageNews = findViewById(R.id.image_news);

        ViewPager2 galary = findViewById(R.id.galary);

        closed.setOnClickListener(v -> {
            onBackPressed();
        });

        viewModel.getNewsDetails().observe(this, news -> {
            titleText.setText(news.getTitle());


            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date = format.parse(news.getPublishedAt());
                createText.setText("Опубліковано: " + new SimpleDateFormat("dd MMMM yyyy", new Locale("uk", "UA")).format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (news.getPhotos() != null && news.getPhotos().size() > 0) {
                galary.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
                galary.setAdapter(new PhotosAdaper(this, news.getPhotos()));
                galary.setVisibility(View.VISIBLE);
                imageNews.setVisibility(View.GONE);
            } else {
                Glide.with(this).load(news.getImageUrl()).error(R.drawable.ic_prew).into(imageNews);
                galary.setVisibility(View.GONE);
                imageNews.setVisibility(View.VISIBLE);
            }

            if (news.getBody() != null) {
                String web = "<html><head><LINK href=\"https://ap.sportforall.gov.ua/images/index.css\" rel=\"stylesheet\"/></head><body>" + news.getBody() + "</body></html>";
                web = web.replace("<img ", "<br><img ").replace("<img>", "");
                html.setMovementMethod(LinkMovementMethod.getInstance());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    html.setText(Html.fromHtml(web, this, null));
                } else {
                    html.setText(Html.fromHtml(web));
                }
            }
        });
    }

    @Override
    public Drawable getDrawable(String source) {
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty = getResources().getDrawable(R.drawable.logo_active_parks);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

        new LoadImage(html.getWidth()).setListener(new LoadImage.Listener() {
            @Override
            public void onUpdate() {
                CharSequence t = html.getText();
                html.setText(t);
            }
        }).execute(source, d);

        return d;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}