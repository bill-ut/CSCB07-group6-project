package com.example.b07demosummer2024.ui.support;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.data.JsonReader;
import com.example.b07demosummer2024.model.Resource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class SupportFragment extends Fragment {

    private static final String CITY_QID = "city";

    private TextView tvHeader;
    private LinearLayout llContainer;
    private Map<String, List<Resource>> cityMap;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_support, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        tvHeader    = view.findViewById(R.id.tvSupportHeader);
        llContainer = view.findViewById(R.id.llResourcesContainer);

        // Load city_resources.json into cityMap
        String json = JsonReader.loadJSONFromAsset(
                requireContext(), "city_resources.json");
        Type type = new TypeToken<Map<String, List<Resource>>>(){}.getType();
        cityMap = new Gson().fromJson(json, type);

        // Fetch and resolve the user's city answer
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .child("answers");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot snap) {
                // Log all keys/values for debugging
                for (DataSnapshot child : snap.getChildren()) {
                    Log.d("SupportFragment",
                            "warmup key=" + child.getKey() +
                                    " value=" + child.getValue());
                }

                // Try reading city as either a String or from an answer array
                DataSnapshot cityNode = snap.child(CITY_QID);
                String city = null;
                if (cityNode.exists()) {
                    Object raw = cityNode.getValue();
                    if (raw instanceof String) {
                        city = (String) raw;
                    } else {
                        DataSnapshot answers = cityNode.child("answer");
                        if (answers.exists()) {
                            for (DataSnapshot item : answers.getChildren()) {
                                city = item.getValue(String.class);
                                break;
                            }
                        }
                    }
                }

                Log.d("SupportFragment", "Resolved city = " + city);
                showResourcesFor(city);
            }

            @Override public void onCancelled(@NonNull DatabaseError e) {
                Log.e("SupportFragment", "Firebase error", e.toException());
                showResourcesFor(null);
            }
        });
    }

    private void showResourcesFor(@Nullable String city) {
        llContainer.removeAllViews();
        tvHeader.setText(
                city != null
                        ? "Resources for " + city
                        : "Resources"
        );

        List<Resource> resources = cityMap.get(city);
        if (resources == null || resources.isEmpty()) {
            TextView tv = new TextView(requireContext());
            tv.setText("No local resources found.");
            llContainer.addView(tv);
            return;
        }

        for (Resource r : resources) {
            TextView entry = new TextView(requireContext());
            entry.setText(r.getName() + "\n" + r.getUrl());
            entry.setAutoLinkMask(Linkify.WEB_URLS);
            entry.setLinksClickable(true);
            entry.setMovementMethod(LinkMovementMethod.getInstance());
            entry.setPadding(0, 16, 0, 16);
            llContainer.addView(entry);
        }
    }
}
