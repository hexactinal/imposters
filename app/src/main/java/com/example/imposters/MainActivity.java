package com.example.imposters;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;

import android.app.AppOpsManager;
import android.os.BatteryManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Logic to display battery data
        Context context = getApplicationContext();

        //Code that determines the current charging status
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                     status == BatteryManager.BATTERY_STATUS_FULL;

        // How are we charging?
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        //Determine the current battery level?
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level * 100 / (float)scale;

        //Display for text
        TextView percentageView = (TextView) findViewById(R.id.percentage);

        percentageView.setText(Float.toString(batteryPct));

        //update image to loosely reflect charge

        //the big battery imageview
        ImageView ImageView = (ImageView) findViewById(R.id.imageView2);
        //the six? battery imgs
        if (batteryPct >= 78){
            ImageView.setImageResource(R.drawable.damagingbatthigh);

        }
        else if (batteryPct < 77){
            ImageView.setImageResource(R.drawable.batthigh);
        }
        else if (batteryPct < 60){
            ImageView.setImageResource(R.drawable.batthighmed);
        }
        else if (batteryPct < 45) {
            ImageView.setImageResource(R.drawable.battlowmed);
        }
        else if (batteryPct < 30) {
            ImageView.setImageResource(R.drawable.battlow);
        }
        else if (batteryPct <=22) {
            ImageView.setImageResource(R.drawable.damagingbattlow);
        }

        //Update text whenever battery changes

        TextView text = (TextView)findViewById(R.id.percentage);
        boolean isChargingStatus = isBatteryCharging(getApplicationContext());
        String chargingMsg = isChargingStatus ? "Your device is charging" : "Your device is NOT charging";
        text.setText(chargingMsg);
        if(!checkPackagePermissions())
            requestPermissions();

    }

    public static boolean isBatteryCharging(Context context) {
        IntentFilter batteryIntent = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, batteryIntent);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean charging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        return charging;
    }
    public boolean checkPackagePermissions(){
        AppOpsManager appOps =  (AppOpsManager)
                getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        if (mode == AppOpsManager.MODE_ALLOWED)
                return true;
        return false;
    }

    //https://medium.com/@quiro91/show-app-usage-with-usagestatsmanager-d47294537dab
    public void requestPermissions(){
        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
    }

    /*if (level > 79){
        <ImageView2> src= dangerousbattfull;
    }*/
}