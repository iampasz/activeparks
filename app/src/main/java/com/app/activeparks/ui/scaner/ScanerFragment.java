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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.app.activeparks.MainActivity;
import com.app.activeparks.ui.clubs.ClubActivity;
import com.app.activeparks.ui.event.activity.EventFragment;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.zxing.Result;
import com.technodreams.activeparks.R;
import com.technodreams.activeparks.databinding.FragmentScanerBinding;

public class ScanerFragment extends Fragment {

    private CodeScanner mCodeScanner;

    private ScanerViewModel mViewModel;

    NavController navControllerHome;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this, new ScanerModelFactory(getContext())).get(ScanerViewModel.class);

        com.technodreams.activeparks.databinding.FragmentScanerBinding binding = FragmentScanerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final CodeScannerView scannerView = binding.scannerView;

        mCodeScanner = new CodeScanner(requireActivity(), scannerView);

        mCodeScanner.setDecodeCallback((Result result) -> requireActivity().runOnUiThread(() -> {
            String code = result.getText();
            if (code.contains("/club/")) {
                if (code.length() > 36) {
                    if (mViewModel.getUser()) {
                        String id = code.substring(result.getText().length() - 36, result.getText().length());
                        mViewModel.activateClubQrCode(id);
                    } else {
                        Toast.makeText(ScanerFragment.this.getContext(), "Потрібно авторизуватися!", Toast.LENGTH_LONG).show();
                    }
                }
            } else if (code.contains("/route-point/")) {
                if (code.length() > 36) {
                    if (mViewModel.getUser()) {
                        String id = code.substring(result.getText().length() - 36, result.getText().length());
                        mViewModel.activatePointQrCode(id);
                    } else {
                        Toast.makeText(ScanerFragment.this.getContext(), "Потрібно авторизуватися!", Toast.LENGTH_LONG).show();
                    }
                }
            } else if (code.contains("/qr/")) {
                if (code.length() > 10) {
                    String id = code.substring(code.indexOf("/qr/"));
                    mViewModel.activateScanerCodeRequest(id);
                }
            } else {
                navControllerHome.navigate(R.id.navigation_active);
            }
        }));

        scannerView.setOnClickListener((View view) -> mCodeScanner.startPreview());

        binding.button.setOnClickListener((View view) -> ((MainActivity) requireActivity()).getNavControllerMain().navigate(R.id.selectVideoFragment));

        mViewModel.getPointQrCode().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                startActivity(new Intent(getContext(), EventFragment.class).putExtra("id", result));
            } else {
                Toast.makeText(getContext(), "QR код для точки не дійсний", Toast.LENGTH_LONG).show();
            }
        });

        mViewModel.getClubQrCode().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                startActivity(new Intent(getContext(), ClubActivity.class).putExtra("id", result));
            } else {
                Toast.makeText(getContext(), "QR код для клубу не дійсний", Toast.LENGTH_LONG).show();
            }
        });

        mViewModel.getSelectVideoQrCode().observe(getViewLifecycleOwner(), id -> {
            if (id != null) {
                //startActivity(new Intent(getActivity(), SelectVideoActivity.class).putExtra("code", id));
                navControllerHome.navigate(R.id.navigation_active);
            } else {
                Toast.makeText(getContext(), "QR код для клубу не дійсний", Toast.LENGTH_LONG).show();
            }
        });

        checkSelfPermissions(getContext());
        return root;
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }


    private void checkSelfPermissions(Context ctx) {
        if (ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{
                            Manifest.permission.CAMERA},
                    1);
        } else {
            mCodeScanner.startPreview();
        }
    }

}