package com.app.activeparks.ui.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.activeparks.ui.auth.AuthFragment;
import com.app.activeparks.util.FragmentInteface;
import com.technodreams.activeparks.R;

public class RegisterFragment extends Fragment {

    private RegisterViewModel mViewModel;

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
        Button sendCode = binding.findViewById(R.id.sendCode);
        Button regAction = binding.findViewById(R.id.reg_action);


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
            mViewModel.getMessage().observe(getViewLifecycleOwner(), msg -> {
                ((FragmentInteface) getActivity()).message(msg);
            });
        });


        return binding;
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}