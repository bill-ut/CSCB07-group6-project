package com.example.b07demosummer2024;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(
                R.layout.activity_home_fragment,   // or your existing home layout file
                container,
                false
        );

        Button logoutBtn = view.findViewById(R.id.logoutButton);
        logoutBtn.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_home_to_login)
        );

        return view;
    }
}
