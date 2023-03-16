package com.app.activeparks.util;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class LoadImage extends AsyncTask<Object, Void, Bitmap> {

    private LevelListDrawable mDrawable;

    public Listener charSequence;
    public int weight;

    public LoadImage(int weight) {
        this.weight = weight;
    }

    @Override
    protected Bitmap doInBackground(Object... params) {
        String source = (String) params[0];
        mDrawable = (LevelListDrawable) params[1];
        try {
            InputStream is = new URL(source).openStream();
            return BitmapFactory.decodeStream(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            BitmapDrawable d = new BitmapDrawable(bitmap);
            mDrawable.addLevel(1, 1, d);
            mDrawable.setBounds(0, 0, weight, (int) (weight / 1.5));
            mDrawable.setLevel(1);
            charSequence.onUpdate();
        }
    }

    public interface Listener{
        void onUpdate();
    }

    public LoadImage setListener(Listener charSequence){
        this.charSequence = charSequence;
        return this;
    }
}