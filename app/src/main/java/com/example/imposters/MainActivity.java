package com.example.imposters;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView text = (TextView)findViewById(R.id.text1);
        boolean isCharging = isBatteryCharging(getApplicationContext());
        String chargingMsg = isCharging ? "Your device is charging" : "Your device is NOT charging";
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

}