package com.app.activeparks.ui.notification.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.notification.Entities;
import com.app.activeparks.data.model.notification.EntitiesArray;
import com.app.activeparks.data.model.notification.EventNotification;
import com.app.activeparks.data.model.notification.ItemNotification;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotificationAdaper extends RecyclerView.Adapter<NotificationAdaper.ViewHolder> {

    private final List<ItemNotification> list;
    private final LayoutInflater inflater;

    public NotificationListener listener;

    public NotificationAdaper(Context context, List<ItemNotification> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemNotification notification = list.get(position);

        if (notification.getMessage() != null) {
            String msg = notification.getMessage();
            int count = 0;

            while (Pattern.compile("[{](.*?)[}]").matcher(msg).find()) {
                if  (notification.getEntities().noll.entityType != null) {
                    msg = msg.replace("{{" + count + "}}", count == 0 ? "<u>" + "<a href='\\" + notification.getEntities().noll.entityType + notification.getEntities().noll.entityId + "'>" + notification.getEntities().noll.entityTitle + "</a>" + "</u>" : "<u>" + "<a href='\\" + notification.getEntities().one.entityType + notification.getEntities().one.entityId + "'>" + notification.getEntities().one.entityTitle + "</a>" + "</u>");
                    count++;
                }
                if (notification.getEntities().one == null) {
                    break;
                }
            }

            setTextViewHTML(holder.description, msg);
        }
        Date secondDate = new Date();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date firstDate = format.parse(notification.getCreatedAt());

            long diffInMilliseconds = Math.abs(secondDate.getTime() - firstDate.getTime());

            long days = TimeUnit.DAYS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);
            long hours = TimeUnit.HOURS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);
            long minutes = TimeUnit.HOURS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);

            if (days > 1){
                holder.date.setText(days + " д. тому");
            }else if (hours > 0.9){
                holder.date.setText(hours +  " год. тому");
            }else{
                holder.date.setText(minutes +  " хв. тому");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                listener.onInfo(span.getURL());
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    protected void setTextViewHTML(TextView text, String html) {
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for (URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }
        text.setText(strBuilder);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView date, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);
        }
    }

    public interface NotificationListener {
        void onInfo(String url);
    }

    public NotificationAdaper setOnCliclListener(NotificationListener listener) {
        this.listener = listener;
        return this;
    }

}
