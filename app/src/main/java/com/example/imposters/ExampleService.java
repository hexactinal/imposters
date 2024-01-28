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
