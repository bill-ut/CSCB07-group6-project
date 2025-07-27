package com.example.b07demosummer2024.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.data.EncryptedPrefsProvider;
import com.google.android.material.textfield.TextInputEditText;

public class PinLoginFragment extends Fragment {

    private TextInputEditText etPin;
    private Button btnSubmit;
    private TextView tvPrompt, tvUsePassword;
    private EncryptedPrefsProvider prefs;
    private boolean createMode;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // Inflate your existing layout (fragment_pin_login.xml)
        return inflater.inflate(R.layout.fragment_pin_login, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        etPin         = view.findViewById(R.id.etPin);
        btnSubmit     = view.findViewById(R.id.btnSubmitPin);
        tvPrompt      = view.findViewById(R.id.tvPinPrompt);
        tvUsePassword = view.findViewById(R.id.tvUsePassword);

        try {
            prefs = new EncryptedPrefsProvider(requireContext());
        } catch (Exception e) {
            Toast.makeText(getContext(),
                    "Error initializing secure storage", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            // fallback to login screen
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_pin_to_login);
            return;
        }

        // Decide mode based on whether a PIN is already saved
        String savedPin = prefs.getPin();
        createMode = TextUtils.isEmpty(savedPin);

        if (createMode) {
            enterCreateMode();
        } else {
            enterVerifyMode(savedPin);
        }
    }

    private void enterCreateMode() {
        tvPrompt.setText("Create a 4-digit PIN");
        btnSubmit.setText("Set PIN");
        tvUsePassword.setVisibility(View.GONE);

        btnSubmit.setOnClickListener(v -> {
            String newPin = etPin.getText().toString().trim();
            if (newPin.length() != 4 || !TextUtils.isDigitsOnly(newPin)) {
                etPin.setError("Enter exactly 4 digits");
                return;
            }
            prefs.savePin(newPin);
            Toast.makeText(getContext(), "PIN saved", Toast.LENGTH_SHORT).show();
            // Navigate to Home (or questionnaire if first login)
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_pin_to_home);
        });
    }

    private void enterVerifyMode(String savedPin) {
        tvPrompt.setText("Enter your PIN");
        btnSubmit.setText("Submit");
        tvUsePassword.setVisibility(View.VISIBLE);

        btnSubmit.setOnClickListener(v -> {
            String input = etPin.getText().toString().trim();
            if (input.equals(savedPin)) {
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_pin_to_home);
            } else {
                Toast.makeText(getContext(),
                        "Incorrect PIN, please try again.", Toast.LENGTH_SHORT).show();
                etPin.getText().clear();
            }
        });

        tvUsePassword.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_pin_to_login)
        );
    }
}
