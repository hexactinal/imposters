package com.example.imposters;

import android.app.Notification;
import android.app.Service;
import android.os.IBinder;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ExampleService extends Service {
    @Override
    public void onCreate(){
        super.onCreate();
    }
    /*@Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return super.onStartCommand(intent, flags, startId);

        String chanelID = "CHANEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), chanelID);
        builder.setSmallIcon(R.drawable.untitled);
        builder.setContentTitle("BATTERY HEALTH ALERT!!!!!! SERIOUS!!!!!!");
        builder.setContentText("YOUR BATTERY IS DANGEROUSLY!!!!!! CHARGE NOW OR STOP CHARGING");
        builder.setAutoCancel(false).setPriority(NotificationCompat.PRIORITY_MAX);

    }*/

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
    @Nullable
    @Override

        public IBinder onBind(Intent intent){
            return null;
        }

}
