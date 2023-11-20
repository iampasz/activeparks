package com.app.activeparks.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.app.activeparks.data.model.Default;
import com.app.activeparks.data.storage.Preferences;
import com.app.activeparks.data.repository.Repository;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.technodreams.activeparks.R;

import org.jetbrains.annotations.Nullable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BottomEmailDialog extends BottomSheetDialogFragment{

    public static BottomEmailDialog newInstance() {
        return new BottomEmailDialog();
    }

    public EditText email, code;
    private Repository repository;


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
        View view = inflater.inflate(R.layout.dialog_email, container,
                false);

        repository = new Repository(new Preferences(getContext()));

        email = view.findViewById(R.id.email);
        code = view.findViewById(R.id.code);


        view.findViewById(R.id.send_code_action).setOnClickListener(v ->{
            repository.sendCodeEmail(email.getText().toString()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> Toast.makeText(getActivity(),"Код надіслано", Toast.LENGTH_SHORT).show(),
                            error -> {
                                try {
                                    Default def = new Gson().fromJson(error.getMessage(), Default.class);
                                    Toast.makeText(getActivity(), def.getErrors().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(),"Перевірте підключення до інтернету", Toast.LENGTH_SHORT).show();
                                }
                            });
            view.findViewById(R.id.status).setVisibility(View.VISIBLE);
            view.findViewById(R.id.edit_phone_action).setEnabled(true);
            view.findViewById(R.id.edit_phone_action).setAlpha(1.0f);
        });

        view.findViewById(R.id.edit_phone_action).setOnClickListener(v ->{
            repository.verifyUserEmailRequest(email.getText().toString(), code.getText().toString()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> Toast.makeText(getActivity(),"Номер змінено", Toast.LENGTH_SHORT).show(),
                            error -> {
                                try {
                                    Default def = new Gson().fromJson(error.getMessage(), Default.class);
                                    Toast.makeText(getActivity(), def.getErrors().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(),"Перевірте підключення до інтернету", Toast.LENGTH_SHORT).show();
                                }
                            });
        });

        return view;
    }
}