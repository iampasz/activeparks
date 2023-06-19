package com.app.activeparks.ui.register;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.activeparks.data.model.Errors;
import com.app.activeparks.ui.auth.AuthFragment;
import com.app.activeparks.util.FragmentInteface;
import com.technodreams.activeparks.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegisterFragment extends Fragment {

    private RegisterViewModel mViewModel;
    private TextView timer, errorNickname, errorPassword, errorEmail, errorCode;
    private Button sendCode, regAction;
    private View binding;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this, new RegisterModelFactory(getContext())).get(RegisterViewModel.class);
        binding = inflater.inflate(R.layout.fragment_register, container, false);

        EditText name = binding.findViewById(R.id.name);
        EditText password = binding.findViewById(R.id.password);
        EditText email = binding.findViewById(R.id.email);
        EditText code = binding.findViewById(R.id.code);

        ImageView closed = binding.findViewById(R.id.closed);
        sendCode = binding.findViewById(R.id.sendCode);
        regAction = binding.findViewById(R.id.reg_action);

        timer = binding.findViewById(R.id.timer);
        errorNickname = binding.findViewById(R.id.login_error);
        errorPassword = binding.findViewById(R.id.password_error);
        errorEmail = binding.findViewById(R.id.email_error);
        errorCode = binding.findViewById(R.id.code_error);


        closed.setOnClickListener(v-> {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_user, new AuthFragment())
                    .commit();
        });

        sendCode.setOnClickListener(v-> {
            mViewModel.sendCode(email.getText().toString());
        });

        regAction.setOnClickListener(v-> {
            mViewModel.signup(name.getText().toString(),
                    password.getText().toString(),
                    email.getText().toString(),
                    code.getText().toString());

            errorNickname.setVisibility(View.GONE);
            errorPassword.setVisibility(View.GONE);
            errorEmail.setVisibility(View.GONE);
            errorCode.setVisibility(View.GONE);
        });

        mViewModel.getMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) {
                if (msg.contains("1")) {
                    sendCode.setEnabled(false);
                    startTimer();
                }else if (msg.contains("2")) {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container_user, new AuthFragment())
                            .commit();
                } else {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                }
            }
        });

        mViewModel.getMessageError().observe(getViewLifecycleOwner(), errors -> {
            for (Errors item : errors) {
                if (item.getParam().contains("nickname")) {
                    errorNickname.setText(item.getMsg());
                    errorNickname.setVisibility(View.VISIBLE);
                }
                if (item.getParam().contains("password")) {
                    errorPassword.setText(item.getMsg());
                    errorPassword.setVisibility(View.VISIBLE);
                }
                if (item.getParam().contains("email")) {
                    errorEmail.setText(item.getMsg());
                    errorEmail.setVisibility(View.VISIBLE);
                }
                if (item.getParam().contains("code")) {
                    errorCode.setText(item.getMsg());
                    errorCode.setVisibility(View.VISIBLE);
                }
            }
        });

        return binding;
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    void startTimer() {
        sendCode.setEnabled(false);
        timer.setVisibility(View.VISIBLE);
            new CountDownTimer(60000, 1000) {

                public void onTick(long millisUntilFinished) {

                    long Seconds = millisUntilFinished / 1000 % 60;
                    timer.setText("Отримати код повторно через " + String.format("%02d", Seconds));
                }

                public void onFinish() {
                    cancel();
                    sendCode.setEnabled(true);
                    timer.setVisibility(View.GONE);

                }
            }.start();
    }
}