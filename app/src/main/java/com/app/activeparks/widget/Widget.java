package com.app.activeparks.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.app.activeparks.MainActivity;
import com.app.activeparks.data.storage.Preferences;
import com.technodreams.activeparks.R;

import java.util.Locale;

public class Widget extends AppWidgetProvider {

    final String LOG_TAG = "LOG_APP";
    private Preferences sharedPreferences;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
//
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activity_widget);
            sharedPreferences = new Preferences(context);
//
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_MUTABLE);
            views.setOnClickPendingIntent(R.id.layout_widget, pendingIntent);
//
//
//            views.setTextViewText(R.id.city, "" + sharedPrefs.getCityName());
//            new RetrofitClient().weather().forecast(sharedPrefs.getLatitude(), sharedPrefs.getLongitude(), "6a588f6f040a74a349efa599e591b270", "metric", Locale.getDefault().getLanguage())
//                    .enqueue(new Callback<WeatherModel>() {
//                        @Override
//                        public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
//                            if (response.isSuccessful() && response.body().getListWeather().size() > 0) {
//                                views.setTextViewText(R.id.main_temperature, "" + Math.round(response.body().getListWeather().get(0).getMain().getTemp()) + "°");
//                                views.setImageViewResource(R.id.status, ImageWeather.image(response.body().getListWeather().get(0).getWeather().get(0).getIcon()));
//                                appWidgetManager.updateAppWidget(appWidgetIds, views);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<WeatherModel> call, Throwable t) {
//                        }
//                    });

    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }


}