package com.example.b07demosummer2024.ui.emergency;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.emergency.EmergencyCategory;

import java.util.List;

public class EmergencyInfoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_emergency_info, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.emergencyCategoryRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<EmergencyCategory> categories = EmergencyCategory.getDefaultCategories();

        EmergencyCategoryAdapter adapter = new EmergencyCategoryAdapter(categories, category -> {
            switch (category.name) {
                case "Emergency Contacts":
                    NavController navController = Navigation.findNavController(requireView());
                    navController.navigate(R.id.action_emergencyInfoFragment_to_emergencyContactFragment);
                    break;
                case "Documents":
                    // TODO: Add navigation to DocumentsFragment
                    break;
                case "Safe Locations":
                    // TODO: Add navigation to SafeLocationsFragment
                    break;
                case "Medications":
                    // TODO: Add navigation to MedicationsFragment
                    break;
            }
        });

        recyclerView.setAdapter(adapter);
        return view;
    }
}
