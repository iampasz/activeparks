package com.app.activeparks.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.technodreams.activeparks.R;

public class TextTurn extends View {

    private int radius = 320;
    //private int startAngle = Integer.MIN_VALUE;
    //private int sweepAngle = Integer.MIN_VALUE;
    private int centerAngle =-90;
    private float textSize = getResources().getDisplayMetrics().density * 16;
    private String text = "";

    private int textColor = Color.WHITE;
    private Typeface fontFamily;

    private Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float offset = 0;

    public TextTurn(Context context) {
        super(context);
    }

    public TextTurn(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TextTurn);

        radius = typedArray.getDimensionPixelSize(R.styleable.TextTurn_radius, radius);
        //startAngle = typedArray.getInteger(R.styleable.TextTurn_start_angle, startAngle);
        //sweepAngle = typedArray.getInteger(R.styleable.TextTurn_sweep_angle, sweepAngle);
        centerAngle = typedArray.getInteger(R.styleable.TextTurn_center_angle, centerAngle);
        textSize = typedArray.getDimensionPixelSize(R.styleable.TextTurn_text_size, (int) textSize);

        String text = typedArray.getString(R.styleable.TextTurn_text);
        if (text != null)
            this.text = text;

        int colorRes = typedArray.getColor(R.styleable.TextTurn_text_color, Color.WHITE);
        if (colorRes != -1)
            textColor = colorRes;

        int fontRes = typedArray.getResourceId(R.styleable.TextTurn_font_family, -1);
        if (fontRes != -1)
            fontFamily = ResourcesCompat.getFont(context, fontRes);

        typedArray.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        offset = textSize * 0.75f;

        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.width = radius > 0 ? (int) (radius * 2 + offset * 2) : 0;
        lp.height = radius > 0 ? (int) (radius * 2 + offset * 2) : 0;
        requestLayout();

        //Text color
        paintText.setColor(textColor);
        paintText.setTypeface(fontFamily);
        paintText.setTextSize(textSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        float textWidth = paintText.measureText(text);
        float circumference = (float) (2 * Math.PI * radius);   //Длина окружности

        float textAngle = textWidth * 360 / circumference;   //Угол занимаемый текстом
        float startAngle = centerAngle - (textAngle / 2);

//        RectF oval = new RectF(offset, offset,radius * 2 + offset,radius * 2 + offset);
//        Path pathArc = new Path();
//        pathArc.addArc(oval, startAngle, 350);
//
//        Path mPath = new Path();
//        mPath.addCircle(x, y, 200, Path.Direction.CW);
//        canvas.drawTextOnPath(text, mPath, 0, 0, paintText);
    }


    //Устанавливаем текст
    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    public void setRadius(int radius) {
        this.radius = radius;
        invalidate();
    }

    //Устанавливаем текст
    public void setCenterAngle(int centerAngle) {
        this.centerAngle = centerAngle;
        invalidate();
    }

    //Устанавливаем цвет текста
    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    //Устанавливаем цвет текста
    public void setTextSize(int textSize) {
        this.textSize = textSize;
        invalidate();
    }

    //Устанавливаем цвет текста
    public void setFontFamily(Typeface fontFamily) {
        this.fontFamily = fontFamily;
        invalidate();
    }

}