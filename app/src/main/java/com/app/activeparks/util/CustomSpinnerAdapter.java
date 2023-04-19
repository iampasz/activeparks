package com.app.activeparks.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.util.Pair;

import java.util.ArrayList;
import java.util.Map;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    private Map<String, String> itemsMap;

    public CustomSpinnerAdapter(Context context, Map<String, String> itemsMap) {
        super(context, android.R.layout.simple_spinner_item, new ArrayList<>(itemsMap.values()));
        this.itemsMap = itemsMap;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) convertView;
        if (textView == null) {
            textView = new TextView(getContext());
        }
        String itemValue = getItem(position);
        textView.setText(itemValue);
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) convertView;
        if (textView == null) {
            textView = new TextView(getContext());
        }
        String itemValue = getItem(position);
        textView.setText(itemValue);
        return textView;
    }

    private String getKeyFromValue(String value) {
        for (Map.Entry<String, String> entry : itemsMap.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }
}