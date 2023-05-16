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
    private TextView textView;
    private FrameLayout copyAction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_qr);

        mViewModel =
                new ViewModelProvider(this, new QrModelFactory(this)).get(QrViewModel.class);

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        findViewById(R.id.closed).setOnClickListener((View v) -> {
            finish();
        });

        String clubId = getIntent().getStringExtra("clubId");

        mQrCode = findViewById(R.id.image_qr);
        copyAction = findViewById(R.id.copy_action);

        textView = findViewById(R.id.url);

        if (getIntent().getBooleanExtra("club", false) == true){
            mViewModel.createQrCodeClub(clubId);
            findViewById(R.id.shared_action).setVisibility(View.VISIBLE);
            textView.setText("https://ap.sportforall.gov.ua/fc/" + clubId);
        }else{
            createCode(getIntent().getStringExtra("pointId"));
            copyAction.setVisibility(View.VISIBLE);
        }

        mViewModel.getQrCode().observe(this, qr -> createCode(qr));

        copyAction.setOnClickListener(v ->{
            ClipData clip = ClipData.newPlainText("", textView.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Скопійовано", Toast.LENGTH_SHORT).show();
        });
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