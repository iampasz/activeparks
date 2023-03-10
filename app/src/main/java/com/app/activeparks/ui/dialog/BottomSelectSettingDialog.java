package com.app.activeparks.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.repository.Repository;
import com.technodreams.activeparks.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.Nullable;

public class BottomSelectSettingDialog extends BottomSheetDialogFragment{

    public static BottomSelectSettingDialog newInstance() {
        return new BottomSelectSettingDialog();
    }

    public EditText searchCity;
    private Repository apiRepository;
    public RecyclerView listCity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_select_setting, container,
                false);

        apiRepository = new Repository();



        return view;

    }


}