package com.app.activeparks.ui.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class BottomPhoneDialog extends BottomSheetDialogFragment {

    public static BottomPhoneDialog newInstance() {
        return new BottomPhoneDialog();
    }

    public Button send, editPhoneAction;

    public EditText phone, code;

    public TextView status;
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
        View view = inflater.inflate(R.layout.dialog_phone, container,
                false);

        repository = new Repository(new Preferences(getContext()));

        send = view.findViewById(R.id.send_code_action);
        editPhoneAction = view.findViewById(R.id.edit_phone_action);

        phone = view.findViewById(R.id.phone);
        code = view.findViewById(R.id.code);

        status = view.findViewById(R.id.status);


        view.findViewById(R.id.send_code_action).setOnClickListener(v -> {
            repository.sendSmsCode(phone.getText().toString()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {Toast.makeText(getActivity(), "Код надіслано", Toast.LENGTH_SHORT).show();
                                send.setEnabled(false);
                                startTime();
                                view.findViewById(R.id.edit_phone_action).setEnabled(true);
                                view.findViewById(R.id.edit_phone_action).setAlpha(1.0f);},
                            error -> {
                                try {
                                    Default def = new Gson().fromJson(error.getMessage(), Default.class);
                                    if (def.getErrors().get(0).getMsg() != null) {
                                        Toast.makeText(getActivity(), def.getErrors().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Невідома помилка", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), "Перевірте підключення до інтернету", Toast.LENGTH_SHORT).show();
                                }
                            });
        });

        view.findViewById(R.id.edit_phone_action).setOnClickListener(v -> {
            repository.verifyUserPhoneRequest(phone.getText().toString(), code.getText().toString()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        Toast.makeText(getActivity(), "Номер змінено", Toast.LENGTH_SHORT).show();
                        dismiss();
                        },
                            error -> {
                                try {
                                    Default def = new Gson().fromJson(error.getMessage(), Default.class);
                                    Toast.makeText(getActivity(), def.getErrors().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), "Помилка запиту", Toast.LENGTH_SHORT).show();
                                }
                            });
        });

        return view;
    }

    public void startTime() {
        status.setVisibility(View.VISIBLE);
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {

                long seconds = millisUntilFinished / 1000 % 60;
                status.setText("Отримати код знову можна через: " + String.format("%02d", seconds) + " сек.");
            }

            public void onFinish() {
                status.setVisibility(View.GONE);
                send.setEnabled(true);
            }
        }.start();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (mListener != null) {
            mListener.onBottomSheetCancel();
        }
    }

    public interface OnBottomSheetCancelListener {
        void onBottomSheetCancel();
    }

    private OnBottomSheetCancelListener mListener;

    public BottomPhoneDialog onListener(OnBottomSheetCancelListener listener) {
        this.mListener = listener;
        return this;
    }
}