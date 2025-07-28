package com.example.b07demosummer2024;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import com.example.b07demosummer2024.data.EncryptedPrefsProvider;

import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

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
