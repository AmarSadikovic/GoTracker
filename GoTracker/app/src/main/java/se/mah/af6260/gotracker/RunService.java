package se.mah.af6260.gotracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Koffe on 2017-02-27.
 */

public class RunService extends Service implements SensorEventListener{

    private MainActivity main;
    private IBinder binder;
    private DBHandler dbHandler;
    private SensorManager sensorManager;
    private Sensor stepDetectorSensor;

    @Override
    public void onCreate() {
        super.onCreate();
        dbHandler = new DBHandler();
        binder = new LocalBinder();
        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null) {
            stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            Log.v("Sensor", "Registered!");
        } else {
            //Sensor finns inte
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        return binder;
    }

    public void setListenerActivity(MainActivity main) {
        this.main = main;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public class LocalBinder extends Binder {
        RunService getService() {
            return RunService.this;
        }
    }
}
