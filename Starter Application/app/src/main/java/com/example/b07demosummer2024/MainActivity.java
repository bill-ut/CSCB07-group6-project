package com.example.b07demosummer2024;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

        Button exitButton = findViewById(R.id.emergencyExitButton);
        exitButton.setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com")));
            finishAffinity();
        });
    }



    @Override
    public void onBackPressed() {
        // Default back behaviour: go up the back stack, or exit
        super.onBackPressed();
    }
}
