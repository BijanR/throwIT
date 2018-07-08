package de.fh_dortmund.throwit.menu;

import android.os.Build;

public class DeviceInformation {

    public String getDeviceName() { // Getting producer and model information
        String producer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(producer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(producer) + " " + model;
        }
    }

    private String capitalize(String cap) { // Capitalize method
        if (cap == null || cap.length() == 0) {
            return "";
        }
        char first = cap.charAt(0);
        if (Character.isUpperCase(first)) {
            return cap;
        } else {
            return Character.toUpperCase(first) + cap.substring(1);
        }
    }
}
