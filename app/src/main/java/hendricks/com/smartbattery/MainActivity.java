package hendricks.com.smartbattery;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    TextView voltageTextView, temperatureTextView, statusTextView, SOCTextView;
    BatteryManager battery;
    int batteryStatusInt;
    float batteryVoltage, batteryTemperature, batterySOC, batteryScale, batteryLevel;
    boolean isCharging, acCharge, usbCharge,isPlugged,isFull;
    RelativeLayout main_layout;
    Context context;
    private Handler batteryHandler;
    private int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        initializeScreen();

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus= context.registerReceiver(null, filter);
        batteryVoltage = (float) (batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)/1000.0);
        batteryTemperature = (float) (batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,-1)/10.0);
        batteryLevel = (float) batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
        batteryScale = (float) batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        batterySOC = (float) batteryLevel/batteryScale;
        voltageTextView.setText(Float.toString(batteryVoltage));
        temperatureTextView.setText(Float.toString(batteryTemperature));
        SOCTextView.setText(Float.toString(batterySOC));

        startService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startService(){
        startService(new Intent(this,BackgroundService.class));
    }

    public void stopService() {
        stopService(new Intent(this, BackgroundService.class));
    }

    private void initializeScreen(){
        voltageTextView = (TextView) findViewById(R.id.voltageTextView);
        temperatureTextView = (TextView) findViewById(R.id.temperatureTextView);
        statusTextView = (TextView) findViewById(R.id.statusTextView);
        SOCTextView = (TextView) findViewById(R.id.SOCTextView);
        battery = (BatteryManager) new BatteryManager();
        main_layout = (RelativeLayout) findViewById(R.id.main_layout);
    }

    private void launchPluggedCheck() {
        Runnable launchPluggedCheck = new Runnable(){
            @Override
            public void run() {
/*
                IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                Intent batteryStatus= context.registerReceiver(null, filter);
                batteryStatusInt = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                usbCharge = batteryStatusInt == BatteryManager.BATTERY_PLUGGED_USB;
                acCharge = batteryStatusInt == BatteryManager.BATTERY_PLUGGED_AC;
                isFull = batteryStatusInt == BatteryManager.BATTERY_STATUS_FULL;
                batteryVoltage = (float) (batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)/1000.0);
                batteryTemperature = (float) (batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,-1)/10.0);
                batteryLevel = (float) batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
                batteryScale = (float) batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                batterySOC = (float) batteryLevel/batteryScale;

                isPlugged = usbCharge || acCharge;


                if (isPlugged){
                    statusTextView.setText("I am Plugged In");
                    voltageTextView.setText(Float.toString(batteryVoltage));
                    temperatureTextView.setText(Float.toString(batteryTemperature));
                    SOCTextView.setText(Float.toString(batterySOC));
                    batteryHandler.postDelayed(this,5000);
                }
                if (!isPlugged || isFull){
                    statusTextView.setText("Unplugged");
                    voltageTextView.setText(Float.toString(batteryVoltage));
                    temperatureTextView.setText(Float.toString(batteryTemperature));
                    SOCTextView.setText(Float.toString(batterySOC));
                    batteryHandler.postDelayed(this,5000);
                }*/
            }

        }; //end runnable
        batteryHandler.post(launchPluggedCheck);

    }//end launchPluggedCheck()
}
