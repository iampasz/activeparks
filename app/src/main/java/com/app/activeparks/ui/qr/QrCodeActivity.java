package com.app.activeparks.ui.qr;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.app.activeparks.ui.selectvideo.SelectVideoModelFactory;
import com.app.activeparks.ui.selectvideo.SelectVideoViewModel;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;
import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QrCodeActivity extends AppCompatActivity {

    private QrViewModel mViewModel;
    private ImageView mQrCode;
    private Bitmap bitmap;
    private QRGEncoder qrgEncoder;

    private TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_qr);

        mViewModel =
                new ViewModelProvider(this, new QrModelFactory(this)).get(QrViewModel.class);

        findViewById(R.id.closed).setOnClickListener((View v) -> {
            finish();
        });

        String clubId = getIntent().getStringExtra("clubId");

        mQrCode = findViewById(R.id.image_qr);

        title = findViewById(R.id.title);

        if (getIntent().getBooleanExtra("club", false) == true){
            mViewModel.createQrCodeClub(clubId);
            title.setText("QR-код для вступу до клубу");
        }else{
            createCode(getIntent().getStringExtra("pointId"));
            findViewById(R.id.shared_action).setVisibility(View.VISIBLE);
        }

        mViewModel.getQrCode().observe(this, qr -> createCode(qr));

    }

    void createCode(String qr){
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();


        Point point = new Point();
        display.getSize(point);

        int width = point.x;
        int height = point.y;
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;
        qrgEncoder = new QRGEncoder(qr, null, QRGContents.Type.TEXT, dimen);

        qrgEncoder.setColorBlack(Color.parseColor("#C5E505"));
        qrgEncoder.setColorWhite(Color.parseColor("#FFFFFF"));

        bitmap = qrgEncoder.getBitmap();
        mQrCode.setImageBitmap(bitmap);

        findViewById(R.id.shared_action).setOnClickListener(v ->{

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("image/*");
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "qr", null);
            i.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
            try {
                startActivity(Intent.createChooser(i, "Qr code"));
            } catch (android.content.ActivityNotFoundException ex) {
                ex.printStackTrace();
            }
        });
    }

}