package com.app.activeparks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.app.activeparks.data.storage.Preferences;
import com.app.activeparks.repository.Repository;
import com.google.android.gms.common.util.IOUtils;
import com.technodreams.activeparks.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class TestUpdate extends AppCompatActivity {

    private Repository repository;
    private ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_update);

        repository = new Repository(new Preferences(this));

        avatar = findViewById(R.id.image_avatars);

        findViewById(R.id.button2).setOnClickListener(v -> {
            //testPush();
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 3);
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            if (data != null) {

                try {
                    // Get file path from URI
                    Uri uri = data.getData();
//                    String filePath = getPathFromURI(uri);
                    File file = saveImageToFile(uri);

                    avatar.setImageURI(uri);

//
                    repository.updateFile(file, "avatar");


                    // Do something with the file
                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        }
    }

    public void testPush() {

        Notification.Builder  builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(getApplicationContext(), "activeparks");
        }
        builder.setSmallIcon(R.drawable.logo_active_parks)
                .setContentTitle("My Notification")
                .setContentText("This is the content of my notification")
                .setPriority(Notification.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }


    private File saveImageToFile(Uri imageUri) {
        // Get a reference to the ContentResolver
        ContentResolver resolver = getContentResolver();
        File file = null;

        try {
            // Open an input stream to read the image data
            InputStream inputStream = resolver.openInputStream(imageUri);

            // Create a new file to save the image to
            file = new File(getFilesDir(), "new_image.jpg");

            // Open an output stream to write the image data to the file
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }


            // Close the streams
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        } catch (Exception e) {
            // Handle the exception
        }

        return file;
    }

    public static void copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }
}