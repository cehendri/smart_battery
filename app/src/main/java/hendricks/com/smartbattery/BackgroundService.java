package hendricks.com.smartbattery;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.lang.System;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import android.text.format.DateFormat;

public class BackgroundService extends Service {

    BatteryManager battery;
    int batteryStatusInt;
    double batteryVoltage, batteryTemperature, batterySOC, batteryScale, batteryLevel, time, batteryCurrent;
    boolean isCharging, acCharge, usbCharge,isPlugged,isFull, isRunning;
    long currentTime;
    Context context = this;

    public BackgroundService() {
    }

    @Override
    public void onStart(Intent intent, int startId) {
        handleStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        handleStart(intent, startId);
        return START_STICKY;
    }

    public void handleStart(Intent intent, int startId) {
        Toast.makeText(this, "Started", Toast.LENGTH_LONG).show();
        Notification notification = new NotificationCompat.Builder(context).setContentTitle("Collecting Battery Data").setSmallIcon(R.drawable.ic_launcher).build();
        Intent notificationIntent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
        startForeground(321, notification);

        Runnable launchService = new Runnable(){
            @Override
            public void run() {
                BatteryDatabase db = new BatteryDatabase(getApplicationContext());
                IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                isRunning = true;

                while (isRunning){
                    Intent batteryStatus= context.registerReceiver(null, filter);
                    batteryStatusInt = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                    usbCharge = batteryStatusInt == BatteryManager.BATTERY_PLUGGED_USB;
                    acCharge = batteryStatusInt == BatteryManager.BATTERY_PLUGGED_AC;
                    isFull = batteryStatusInt == BatteryManager.BATTERY_STATUS_FULL;
                    batteryVoltage = (double) (batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)/1000.0);
                    batteryTemperature = (double) (batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,-1)/10.0);
                    batteryLevel = (double) batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
                    batteryScale = (double) batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                    batterySOC = (double) batteryLevel/batteryScale;
                    //temp
                    android.text.format.DateFormat df = new android.text.format.DateFormat();
                    String time = (String) df.format("MM-dd-yyyy kk:mm:ss", new java.util.Date());

                    batteryCurrent = 1.0;
                    BatteryManager bm = new BatteryManager();
                    if (Build.VERSION.SDK_INT>=21) {
                        batteryCurrent = (double) bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);
                    }
                    else {
                        batteryCurrent = 0.0;
                    }
                    isPlugged = usbCharge || acCharge;

                    //Add to Database
                    try{db.addEntry(time, batteryVoltage, batteryTemperature, batterySOC, batteryCurrent );}
                    catch (Exception e){}

                    //Sleep for 15 seconds
                    try {
                        Log.v("Test",Double.toString(batteryVoltage));
                        Thread.sleep(15000);
                    } catch (InterruptedException e) {}
                } //end while loop
            }//end run()

        }; //end runnable

        Thread background = new Thread(launchService);
        background.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
