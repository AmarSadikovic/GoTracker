package se.mah.af6260.gotracker;

import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity {

    public RunService runService;
    private MyServiceConnection serviceConnection;
    private DBHandler dbHandler;
    private Intent serviceIntent;
    public boolean serviceBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new DBHandler();
        serviceConnection = new MyServiceConnection(this);
        serviceIntent = new Intent(this, RunService.class);
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        Log.v("Pedometer", "service bound!");
        Toast.makeText(this, "Step Detector Service bound!", Toast.LENGTH_SHORT).show();
    }
}
