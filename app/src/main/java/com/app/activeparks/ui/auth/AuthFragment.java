package com.app.activeparks.ui.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.activeparks.ui.register.RegisterFragment;
import com.app.activeparks.ui.user.UserFragment;
import com.app.activeparks.ui.restore.RestoreFragment;
import com.technodreams.activeparks.R;

public class AuthFragment extends Fragment {

    private AuthViewModel viewModel;

    private View binding;

    public static AuthFragment newInstance() {
        return new AuthFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, new AuthModelFactory(getContext())).get(AuthViewModel.class);
        binding = inflater.inflate(R.layout.fragment_auth, container, false);

        Button autch = binding.findViewById(R.id.aut_action);
        EditText email = binding.findViewById(R.id.email);
        EditText password = binding.findViewById(R.id.password);
        TextView regAction = binding.findViewById(R.id.reg_action);
        TextView restoreAction = binding.findViewById(R.id.restore_action);
        TextView directoryAction = binding.findViewById(R.id.directory_action);
        TextView selectServer = binding.findViewById(R.id.select_server);

        autch.setOnClickListener(v-> {
            viewModel.login(email.getText().toString(), password.getText().toString());
        });

        regAction.setOnClickListener(v-> {
            replaceFragment(new RegisterFragment());
        });

        restoreAction.setOnClickListener(v-> {
            replaceFragment(new RestoreFragment());
        });

        directoryAction.setOnClickListener(v-> {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://ap.sportforall.gov.ua/infolist/start")));
        });

        viewModel.getMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg.getMessage() != null && msg.getStatus() == false) {
                showMessage(msg.getMessage());
            }else {
                replaceFragment(new UserFragment());
            }
        });

        selectServer.setOnClickListener(v-> {
            if (viewModel.getServer() == true) {
                Toast.makeText(getActivity(),"Тестовий сервер виключений", Toast.LENGTH_SHORT).show();
                viewModel.setServer(false);
            }else{
                Toast.makeText(getActivity(),"Тестовий сервер включений", Toast.LENGTH_SHORT).show();
                viewModel.setServer(true);
            }
        });

        return binding;
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    void replaceFragment(Fragment fragment){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_user, fragment)
                .commit();
    }

    public void showMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}