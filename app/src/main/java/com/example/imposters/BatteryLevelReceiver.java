package com.example.imposters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BatteryLevelReceiver extends BroadcastReceiver {
    public void onReceive(Context context , Intent intent) {
        String action = intent.getAction();

        if(action.equals(Intent.ACTION_BATTERY_LOW)) {
            // Do something when power connected
        }
        else if(action.equals(Intent.ACTION_BATTERY_OKAY)) {
            // Do something when power disconnected
        }
    }
}
