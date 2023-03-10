package com.app.activeparks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.app.activeparks.data.storage.Preferences;
import com.app.activeparks.repository.Repository;
import com.technodreams.activeparks.R;

import java.io.File;
import java.io.IOException;

public class TestUpdate extends AppCompatActivity {

    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_update);

        repository = new Repository(new Preferences(this));

        findViewById(R.id.button2).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 3);
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
//
//
//                String[] proj = { MediaStore.Images.Media.DATA };
//                Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
//                cursor.moveToFirst();
//                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA ;
//                assert cursor != null;
//                String filePath = cursor.getString(columnIndex);

                File file = new File(getRealPathFromURI(uri));

                //repository.updateFileTest(file, "avatar");

//                cursor.close();

            }
        }
    }



    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}