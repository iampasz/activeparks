package com.app.activeparks.ui.restore;

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

import com.app.activeparks.ui.auth.AuthFragment;
import com.app.activeparks.util.FragmentInteface;
import com.technodreams.activeparks.R;

public class RestoreFragment extends Fragment {

    private RestoreViewModel mViewModel;

    private View binding;
    private TextView status;

    public static RestoreFragment newInstance() {
        return new RestoreFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(RestoreViewModel.class);
        binding = inflater.inflate(R.layout.fragment_restore, container, false);

        EditText email = binding.findViewById(R.id.email);
        EditText code = binding.findViewById(R.id.code);
        EditText password = binding.findViewById(R.id.password);

        ImageView closed = binding.findViewById(R.id.closed);
        Button sendCode = binding.findViewById(R.id.sendCode);
        Button restore = binding.findViewById(R.id.restore_action);

        status = binding.findViewById(R.id.status);

        closed.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_user, new AuthFragment())
                    .commit();
        });

        sendCode.setOnClickListener(v -> {
            mViewModel.sendCode(email.getText().toString());
        });

        restore.setOnClickListener(v -> {
            mViewModel.restoreCode(email.getText().toString(), code.getText().toString(), password.getText().toString());
        });

        mViewModel.getErroMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg.getError() == null && msg.getErrors() == null) {
                code.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                restore.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
                sendCode.setVisibility(View.GONE);
                startTime();
            } else {
                if (msg.getErrors() != null) {
                    showMessage(msg.getErrors().get(0).getMsg());
                }else {
                    showMessage(msg.getError());
                }
            }
        });

        mViewModel.getRestoreCode().observe(getViewLifecycleOwner(), msg -> {
            if (msg.getErrors() == null) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_user, new AuthFragment())
                        .commit();
            } else {
                ((FragmentInteface) getActivity()).message(msg.getErrors().get(0).getMsg());
            }
        });

        return binding;
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
            }
        }.start();
    }

    public void showMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}