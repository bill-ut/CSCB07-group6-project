package com.example.b07demosummer2024;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import com.example.b07demosummer2024.data.EncryptedPrefsProvider;

import android.util.Log;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.Toast;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "reminder_channel",                         // Channel ID (must match ReminderReceiver)
                    "Reminder Channel",                         // Channel Name
                    NotificationManager.IMPORTANCE_HIGH         // Importance
            );
            channel.setDescription("Channel for safety plan reminders");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        showDisclaimerDialog();

        Log.d("MainActivity", "onCreate called");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1001);
            }
        }

        createNotificationChannel();

        Log.d("MainActivity", "onCreate() start");

        NavHostFragment navHost = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHost.getNavController();

        NavGraph graph = navController.getNavInflater()
                .inflate(R.navigation.nav_graph);
        try {
            EncryptedPrefsProvider prefs = new EncryptedPrefsProvider(this);
            boolean hasPin = prefs.getPin() != null;
            Log.d("MainActivity", "hasPin = " + hasPin);
            graph.setStartDestination(
                    hasPin ? R.id.pinLoginFragment : R.id.loginFragment
            );
        } catch (Exception e) {
            Log.e("MainActivity", "Error checking PIN", e);
            graph.setStartDestination(R.id.loginFragment);
        }

        navController.setGraph(graph);

        setupExitButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupExitButton();
        // Ensure button is on top after fragment changes
        Button exitButton = findViewById(R.id.emergencyExitButton);
        if (exitButton != null) {
            exitButton.bringToFront();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupExitButton() {
        Button exitButton = findViewById(R.id.emergencyExitButton);
        if (exitButton != null) {
            exitButton.setHapticFeedbackEnabled(false);
            exitButton.setSoundEffectsEnabled(false);

            exitButton.bringToFront();

            exitButton.setOnClickListener(v -> {
                Log.d("MainActivity", "Exit button clicked at: " + System.currentTimeMillis());

                // Disable the button immediately to prevent multiple clicks
                v.setEnabled(false);

                // Launch browser FIRST
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                // Small delay to ensure browser launches before termination
                new Handler().postDelayed(() -> {
                    finishAffinity();
                    System.exit(0);
                }, 100);
            });
        }
    }

    private void showDisclaimerDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Important Notice")
                .setMessage("This app is NOT a substitute for emergency services. In case of immediate danger, call 911.\n\n" +
                        "Safety plans are personal tools and cannot guarantee prevention of harm. " +
                        "Please seek professional help when needed.")
                .setPositiveButton("I Understand", null)
                .setCancelable(false)
                .show();
    }

}
