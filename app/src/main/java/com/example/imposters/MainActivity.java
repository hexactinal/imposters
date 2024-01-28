package com.example.imposters;

import android.Manifest;
import android.app.AppOpsManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private TextView percentageView;

    private EditText editTextInput;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //createNotificationChannel();

        setContentView(R.layout.activity_main);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS) != (PackageManager.PERMISSION_GRANTED)){

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }


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
        //send notif
            makeNotification();

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
        //send notif
            makeNotification();
        }
//
//        LocalTime firstBat = LocalTime.of(0,0,0);
//        LocalTime secondBat = LocalTime.of(0,0,0);
//        if(batteryPct == 80){
//            firstBat = LocalTime.now();
//        }
//        if(batteryPct == 80){
//            secondBat = LocalTime.now();
//        }
//        long elapsedMinutes;
//        LocalTime difference;
//        if (secondBat != null && firstBat != null) {
//            elapsedMinutes = Duration.between(firstBat, secondBat).toMinutes();
//        }

        setBatteryPercent(batteryPct);

//        boolean isChargingStatus = isBatteryCharging(getApplicationContext());
//        String chargingMsg = isChargingStatus ? "Your device is charging" : "Your device is NOT charging";
//        percentageView.setText(chargingMsg);

//        if (!checkPackagePermissions())
//            requestPermissions();
    }



        //Update text whenever battery changes
/*Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
} else {
    //deprecated in API 26
    v.vibrate(500);
}*/


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
                        makeNotification();

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
                        makeNotification();
                    }

                    setBatteryPercent(batteryPct);

                    //display a message or something
                    Toast.makeText(getApplicationContext(), "Battery Level Refreshed :)", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void makeNotification(){


        String chanelID = "CHANEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), chanelID);
        builder.setSmallIcon(R.drawable.untitled);
        builder.setContentTitle("YOUR BATTERY IS BEING DAMAGED!");
        builder.setContentText("YOUR BATTERY IS BEING DAMAGED!!!!!! CHARGE NOW OR STOP CHARGING");
        builder.setAutoCancel(false).setPriority(NotificationCompat.PRIORITY_MAX);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("data", "some value to be passed her");

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);

        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(chanelID);

                if (notificationChannel ==  null) {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    notificationChannel = new NotificationChannel(chanelID, "some name", importance);
                    notificationChannel.setLightColor(Color.rgb(232, 52, 235));
                    notificationChannel.enableVibration(true);
                    notificationManager.createNotificationChannel(notificationChannel);
                }

                //start service on launch end service on notif, restart on refresh button


        }

        notificationManager.notify(0, builder.build());
    }

    public void startService(View V){
        Intent serviceIntent = new Intent(this, ExampleService.class);

        startService(serviceIntent);
    }
    public void stopService(View v){
        Intent serviceIntent = new Intent(this, ExampleService.class);

        stopService(serviceIntent);
    }

    private void setBatteryPercent(float batteryPct) {
        if (percentageView == null) {
            percentageView = (TextView) findViewById(R.id.percentage);
        }

        percentageView.setText(String.valueOf(batteryPct));
    }

}