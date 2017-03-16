package se.mah.af6260.gotracker;


import android.app.AlertDialog;
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
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import java.util.List;
import android.os.Handler;

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
    private TabLayout tabLayout;
    private Session selectedSession;

    private boolean isSessionStarted = false;


    public boolean isStepSensorPresent() {

        return isStepSensorPresent;
    }

    public boolean isGpsSensorPresent() {

        return isGpsSensorPresent;
    }

    public void setIsSessionStarted(boolean bool){
        isSessionStarted = bool;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sf = new StartFrag();
        rf = new RunFrag();
        mapFrag = new MapFrag();
        sessionsFrag = new SessionsFrag();
        dbHandler = new DBHandler(this, null, null, 1);
        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        setFragment(mapFrag, false);
        checkSensorStatus();

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                if(pos == 0){
                    if(!isSessionStarted) {
                        setFragment(mapFrag, false);
                    } else{
                        tabLayout.getTabAt(2).select();
                    }

                } else if (pos == 1){
                    if(!isSessionStarted) {
                        setFragment(sessionsFrag, false);
                    } else {
                        tabLayout.getTabAt(2).select();
                    }

                } else if (pos == 2){
                    if(!isSessionStarted) {
                        setStartFrag();
                    } else {
                        tabLayout.getTabAt(2).select();
                    }
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
    public void setSelectedSession(Session session){
        this.selectedSession = session;
    }

    public Session getSelectedSession(){
        return selectedSession;
    }

    public void changeTab(){
        tabLayout.getTabAt(2).select();
        setStartFrag();
    }

    public DBHandler getDBReference(){
        return dbHandler;
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

    public void setSessionsFrag(){
        setFragment(sessionsFrag, false);
    }

    public String getActivityType(){
        String ret = "";
        if(isRunning){
            ret = "running";
        } else if(isWalking){
            ret = "walking";
        } else if(isCycling){
            ret = "cycling";
        }
        return ret;
    }

    public void setStartFrag(){
        sf = new StartFrag();
        setFragment(sf, false);
        checkSensorStatus();
        sf.sensorStatus(isGpsSensorPresent, isStepSensorPresent);
    }


    public void updateMap(LatLng pos){
        if(pos != null) {
            rf.updateMap(pos);
        } else {
            Toast.makeText(this, "GPS not found", Toast.LENGTH_SHORT).show();
        }
    }

    public LatLng getLocation(){
        return runService.getLocation(this, this);
    }

    public LatLng getLocationMapFrag(Context context, Activity activity){
        int status = context.getPackageManager().checkPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION, context.getPackageName());
        if (status == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        while (status == PackageManager.PERMISSION_DENIED) {
            if (context.getPackageManager().checkPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                status = PackageManager.PERMISSION_GRANTED;
            }
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getAllProviders();

        if (providers != null && providers.contains(LocationManager.NETWORK_PROVIDER)) {
            Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (loc != null) {
                return new LatLng(loc.getLatitude(), loc.getLongitude());
            }
        }
        return null;
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
        } else {
            fm.popBackStack();
        }
        transaction.commit();
        fm.executePendingTransactions();
    }


    public boolean isCycling() {
        return isCycling;
    }

    public void setRunning(boolean running) {isRunning = running;}

    public void setWalking(boolean walking) {isWalking = walking;}

    public void setCycling(boolean cycling) {isCycling = cycling;}

    private boolean isRunning = true, isWalking = false, isCycling = false;

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        //Checking for fragment count on backstack
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this,"Press back again to leave", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
            return;
        }
    }

}
