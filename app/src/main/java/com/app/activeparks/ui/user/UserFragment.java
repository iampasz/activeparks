package com.app.activeparks.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.activeparks.ui.auth.AuthFragment;
import com.app.activeparks.ui.profile.ProfileFragment;
import com.technodreams.activeparks.R;
import com.technodreams.activeparks.databinding.FragmentUserBinding;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private UserViewModel userViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userViewModel =
                new ViewModelProvider(this, new UserModelFactory(getActivity())).get(UserViewModel.class);

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (userViewModel.getUserAuth()){
            show(new ProfileFragment());
        }else {
            show(new AuthFragment());
        }


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void show(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_user, fragment)
                .commit();
    }

    public void stater(View view) {

    }
}