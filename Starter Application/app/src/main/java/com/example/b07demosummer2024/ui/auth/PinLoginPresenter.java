package com.example.b07demosummer2024.ui.auth;

import com.example.b07demosummer2024.data.EncryptedPrefsProvider;

public class PinLoginPresenter {
    private final EncryptedPrefsProvider prefs;
    private PinLoginView view;

    public PinLoginPresenter(EncryptedPrefsProvider prefs) {
        this.prefs = prefs;
    }

    public void attachView(PinLoginView view) {
        this.view = view;
    }

    public void verifyPin(String inputPin) {
        String savedPin = prefs.getPin();
        if (savedPin != null && savedPin.equals(inputPin)) {
            view.onPinVerified();
        } else {
            view.onPinFailed("Incorrect PIN, please try again.");
        }
    }

    public void savePin(String pin) {
        prefs.savePin(pin);
    }
}
