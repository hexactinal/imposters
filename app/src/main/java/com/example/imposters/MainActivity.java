package com.example.imposters;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private TextView percentageView;

    private float startBatteryLevel;
    private boolean fullyCharged = false;
    private long startTime;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshPercent();
        //Logic to display battery data
        Context context = getApplicationContext();

        //Code that determines the current charging status
        BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = isBatteryCharging(getApplicationContext());

        // How are we charging?
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        //Determine the current battery level?
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        //ik this is dumb just go w it plz :)
        float batteryPct = batLevel;//level * 100 / (float) scale;

        //Display for text

        //update image to loosely reflect charge
        //the big battery imageview

        ImageView ImageView = (ImageView) findViewById(R.id.imageView2);
        //the six? battery imgs...
        if (batteryPct >= 78) {
            ImageView.setImageResource(R.drawable.damagingbatthigh);

        } else if (batteryPct < 77 && batteryPct >= 60) {
            ImageView.setImageResource(R.drawable.batthigh);
        } else if (batteryPct < 60 && batteryPct >= 45) {
            ImageView.setImageResource(R.drawable.batthighmed);
        } else if (batteryPct < 45 && batteryPct >= 30) {
            ImageView.setImageResource(R.drawable.battlowmed);
        } else if (batteryPct < 30 && batteryPct > 22) {
            ImageView.setImageResource(R.drawable.battlow);
        } else if (batteryPct <= 22) {
            ImageView.setImageResource(R.drawable.damagingbattlow);
        }

        setBatteryPercent(batteryPct);
        displayTime(batteryPct);
    }

    //Function to display the battery time remaining for the device
    private void displayTime(float batteryPct) {
        TextView timeText = (TextView) findViewById(R.id.textView);
        // Calculate remaining time for full charge
        //int timeRemaining = (int) ((scale * (1 - batteryPct)) / (level / 1000f));
        //Discharging Time=Battery Capacity*Battery Volt/Device Watt.
        if (!fullyCharged) {
            if (batteryPct < 100) {
                if (startBatteryLevel == 0) {
                    startBatteryLevel = batteryPct;
                    startTime = System.currentTimeMillis();
                } else {
                    long currentTime = System.currentTimeMillis();
                    long elapsedTime = currentTime - startTime;
                    float dischargeRate = (startBatteryLevel - batteryPct);

                    // Estimate remaining time
                    float remainingTime = (100 - batteryPct) / dischargeRate;

                    // Display remaining time
                    timeText.setText("Battery Time Remaining " + (int) remainingTime + " minutes");

                }
            } else {
                //Battery fully charged
                fullyCharged = true;
                timeText.setText("Battery Charged Fully");
            }
        }
    }

    //Update text whenever battery changes
    public static boolean isBatteryCharging(Context context) {
        IntentFilter batteryIntent = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, batteryIntent);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean charging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        return charging;
    }

    public boolean checkPackagePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOps.checkOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(),
                    getPackageName()
            );

            return mode == AppOpsManager.MODE_ALLOWED;
        }
        return true;
    }

    //https://medium.com/@quiro91/show-app-usage-with-usagestatsmanager-d47294537dab
    public void requestPermissions() {
        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
    }


    //Button to refresh percentage level
    // -> https://www.geeksforgeeks.org/button-in-kotlin/
    // -> https://developer.android.com/guide/topics/ui/notifiers/toasts#Basics
    public void refreshPercent() {
        Button button = (Button) findViewById(R.id.refreshButton);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View it) {

                    Context context = getApplicationContext();
                    BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
                    int batteryPct = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

                    ImageView ImageView = (ImageView) findViewById(R.id.imageView2);

                    if (batteryPct >= 78) {
                        ImageView.setImageResource(R.drawable.damagingbatthigh);

                    } else if (batteryPct < 77 && batteryPct >= 60) {
                        ImageView.setImageResource(R.drawable.batthigh);
                    } else if (batteryPct < 60 && batteryPct >= 45) {
                        ImageView.setImageResource(R.drawable.batthighmed);
                    } else if (batteryPct < 45 && batteryPct >= 30) {
                        ImageView.setImageResource(R.drawable.battlowmed);
                    } else if (batteryPct < 30 && batteryPct > 22) {
                        ImageView.setImageResource(R.drawable.battlow);
                    } else if (batteryPct <= 22) {
                        ImageView.setImageResource(R.drawable.damagingbattlow);
                    }

                    setBatteryPercent(batteryPct);

                    //display a message or something
                    Toast.makeText(getApplicationContext(), "Battery Level Refreshed :)", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setBatteryPercent(float batteryPct) {
        if (percentageView == null) {
            percentageView = (TextView) findViewById(R.id.percentage);
        }

        percentageView.setText(String.valueOf(batteryPct));
    }
}
