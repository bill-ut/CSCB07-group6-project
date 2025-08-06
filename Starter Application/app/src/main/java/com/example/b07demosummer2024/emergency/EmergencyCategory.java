package com.example.b07demosummer2024.emergency;

import java.util.ArrayList;
import java.util.List;

public class EmergencyCategory {
        public String name;
        public int iconResId;

        public EmergencyCategory(String name, int iconResId) {
            this.name = name;
            this.iconResId = iconResId;
        }

    public static List<EmergencyCategory> getDefaultCategories() {
        List<EmergencyCategory> list = new ArrayList<>();
        list.add(new EmergencyCategory("Documents", android.R.drawable.ic_menu_agenda));
        list.add(new EmergencyCategory("Emergency Contacts", android.R.drawable.ic_menu_call));
        list.add(new EmergencyCategory("Safe Locations", android.R.drawable.ic_menu_mylocation));
        list.add(new EmergencyCategory("Medications", android.R.drawable.ic_menu_info_details));
        return list;
    }
}
