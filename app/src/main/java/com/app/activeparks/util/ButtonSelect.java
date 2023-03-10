package com.app.activeparks.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.technodreams.activeparks.R;
import com.google.android.material.button.MaterialButton;

public class ButtonSelect extends MaterialButton{


    public ButtonSelect(@NonNull Context context) {
        super(context);
    }

    public ButtonSelect(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtonSelect(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MaterialButton on(){
        this.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.button_color_black)));
        this.setIconTint(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.text_color_light)));
        this.setTextColor(getContext().getResources().getColor(R.color.text_color_light));
        return this;
    }

    public MaterialButton off(){
        this.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.button_color_gray)));
        this.setIconTint(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.text_color)));
        this.setTextColor(getContext().getResources().getColor(R.color.text_color));
        return this;
    }
}
