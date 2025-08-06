package com.example.b07demosummer2024.data;

import android.content.Context;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import java.io.IOException;
import java.security.GeneralSecurityException;
import android.content.SharedPreferences;

/**
 * Encapsulates retrieval and storage of user set pin.
 */
public class EncryptedPrefsProvider {

    private final SharedPreferences prefs;

    /**
     * Builds the {@link SharedPreferences} interface.
     *
     * @param ctx The context to use.
     * @throws GeneralSecurityException Security failure.
     * @throws IOException I/O operation failure.
     */
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

    /**
     * Saves the pin to <code>prefs</code>.
     * @param pin The pin to save.
     */
    public void savePin(String pin) {
        prefs.edit().putString("user_pin", pin).apply();
    }

    /**
     * Gets the pin from <code>prefs</code>.
     * @return The user pin.
     */
    public String getPin() {
        return prefs.getString("user_pin", null);
    }
}
