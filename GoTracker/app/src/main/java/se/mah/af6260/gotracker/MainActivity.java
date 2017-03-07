package se.mah.af6260.gotracker;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import java.util.List;

public class MainActivity extends Activity {

    public RunService runService;
    private MyServiceConnection serviceConnection;
    private DBHandler dbHandler;
    private Intent serviceIntent;
    public boolean serviceBound;
    private SensorManager sensorManager;
    private StartFrag sf;
    private RunFrag rf;
    private MapFrag mapFrag;
    private SessionsFrag sessionsFrag;
    private boolean isStepSensorPresent = false;
    private boolean isGpsSensorPresent = false;

    public boolean isStepSensorPresent() {

        return isStepSensorPresent;
    }

    public boolean isGpsSensorPresent() {

        return isGpsSensorPresent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sf = new StartFrag();
        rf = new RunFrag();
        mapFrag = new MapFrag();
        sessionsFrag = new SessionsFrag();
        dbHandler = new DBHandler();
        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        setFragment(sf, false);
        checkSensorStatus();
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                if(pos == 0){
                    setStartFrag();

                } else if (pos == 1){
                    setFragment(sessionsFrag, false);

                } else if (pos == 2){
                    setFragment(mapFrag, false);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void checkSensorStatus(){
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null) {
            isStepSensorPresent = true;
        }else{
            isStepSensorPresent = false;
        }
        LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if (manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            isGpsSensorPresent = true;
        }else{
            isGpsSensorPresent = false;
        }
    }
    public void bindRunService(){
        serviceConnection = new MyServiceConnection(this);
        serviceIntent = new Intent(this, RunService.class);
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbindRunService(){
        if (serviceBound) {
            unbindService(serviceConnection);
            serviceBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        if (serviceBound) {
            unbindService(serviceConnection);
            serviceBound = false;
        }

        super.onDestroy();
    }

    public void setRunFrag(){

        checkSensorStatus();
        if(isGpsSensorPresent && isStepSensorPresent) {
            bindRunService();
            rf = new RunFrag();
            setFragment(rf, false);
        }else{
            Toast.makeText(this, "GPS not found", Toast.LENGTH_SHORT).show();
            sf.sensorStatus(isGpsSensorPresent, isStepSensorPresent);
        }
    }

    public void setStartFrag(){
        sf = new StartFrag();
        setFragment(sf, false);
        checkSensorStatus();
        sf.sensorStatus(isGpsSensorPresent, isStepSensorPresent);
    }

    public void updateSteps(){
        rf.updateSteps();
    }

    public void setFragment(Fragment frag, boolean backstack){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl, frag);
        if(backstack){
            transaction.addToBackStack(null);
        }
        transaction.commit();
        fm.executePendingTransactions();
    }

    public LatLng getLocation(Context context, Activity activity) {
        int status = context.getPackageManager().checkPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION, context.getPackageName());
        if (status == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        while (status == PackageManager.PERMISSION_DENIED) {
            if (context.getPackageManager().checkPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                status = PackageManager.PERMISSION_GRANTED;
            }
        }
        LocationManager mgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = mgr.getAllProviders();
        if (providers != null && providers.contains(LocationManager.NETWORK_PROVIDER)) {
            Location loc = mgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (loc != null) {
                return new LatLng(loc.getLatitude(), loc.getLongitude());
            }
        }
        return new LatLng(55.6910332, 13.1791068); //Home position
    }
}
