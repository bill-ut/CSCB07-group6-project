package com.example.b07demosummer2024.data;

import android.content.Context;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import java.io.IOException;
import java.security.GeneralSecurityException;
import android.content.SharedPreferences;

public class EncryptedPrefsProvider {
    private SharedPreferences prefs;

    public EncryptedPrefsProvider(Context ctx) throws GeneralSecurityException, IOException {
        String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

        prefs = EncryptedSharedPreferences.create(
                "pin_prefs",
                masterKeyAlias,
                ctx,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    public void savePin(String pin) {
        prefs.edit().putString("user_pin", pin).apply();
    }

    public String getPin() {
        return prefs.getString("user_pin", null);
    }
}
