package com.app.activeparks.ui.scaner;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.app.activeparks.ui.clubs.ClubActivity;
import com.app.activeparks.ui.event.EventActivity;
import com.app.activeparks.ui.event.EventScanerListener;
import com.app.activeparks.ui.profile.EditProfileActivity;
import com.app.activeparks.ui.selectvideo.SelectVideoActivity;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.technodreams.activeparks.R;
import com.technodreams.activeparks.databinding.FragmentScanerBinding;
import com.google.zxing.Result;

public class ScanerBottomFragment extends BottomSheetDialogFragment {

    private EventScanerListener eventScanerListener;
    private FragmentScanerBinding binding;
    private CodeScanner mCodeScanner;

    private ScanerViewModel mViewModel;
//
//    public  static ScanerBottomFragment newInstance() {
//        return new ScanerBottomFragment();
//    }

    public ScanerBottomFragment(EventScanerListener eventScanerListener){
        this.eventScanerListener = eventScanerListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

        mViewModel =
                new ViewModelProvider(this, new ScanerModelFactory(getContext())).get(ScanerViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentScanerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final CodeScannerView scannerView = binding.scannerView;
        binding.button.setVisibility(View.GONE);

        mCodeScanner = new CodeScanner(getActivity(), scannerView);

        mCodeScanner.setDecodeCallback((Result result) -> {
            String code = result.getText();
             if (code.contains("/route-point/")) {
                 if (code.length() > 36) {
                     if (mViewModel.getUser() == true) {
                         String id = code.substring(result.getText().length() - 36, result.getText().length());
                         mViewModel.activatePointQrCode(id);
                     } else {
                         Toast.makeText(getContext(), "Потрібно авторизуватися!", Toast.LENGTH_LONG).show();
                     }
                 }
             }

        });
        scannerView.setOnClickListener((View view) -> {
            mCodeScanner.startPreview();
        });

        mViewModel.getPointQrCode().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                eventScanerListener.update();
                dismiss();
            } else {
                Toast.makeText(getContext(), "QR код для точки не дійсний", Toast.LENGTH_LONG).show();
            }
        });

        checkSelfPermissions(getContext());
        return root;
    }

    @Override
    public void onPause() {
        mCodeScanner.stopPreview();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        eventScanerListener.update();
    }


    private void checkSelfPermissions(Context ctx) {
        if (ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.CAMERA},
                    1);
            return;
        } else {
            mCodeScanner.startPreview();
        }
    }


}
