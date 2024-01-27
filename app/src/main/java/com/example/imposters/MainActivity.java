package com.example.imposters;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.content.Context;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;
import java.util.Calendar;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView text = (TextView)findViewById(R.id.text1);
        boolean isCharging = isBatteryCharging(getApplicationContext());
        String chargingMsg = isCharging ? "Your device is charging" : "Your device is NOT charging";
        text.setText(chargingMsg);

        //FIX LATER!
        /*
        if (!checkPackagePermissions()) {
            requestPermissions();
        }
        */

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

        int mode = appOps.checkOp(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        //returns 3


        return AppOpsManager.MODE_ALLOWED == mode;

    }

    //https://medium.com/@quiro91/show-app-usage-with-usagestatsmanager-d47294537dab
    public void requestPermissions(){
        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
    }
    public void usageStats(Context context, long startMils, long endMils){
        UsageStatsManager usageStats = (UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
        Map<String, UsageStats> lUsageStatsMap = usageStats.
                queryAndAggregateUsageStats(startMils, endMils);

    }

}