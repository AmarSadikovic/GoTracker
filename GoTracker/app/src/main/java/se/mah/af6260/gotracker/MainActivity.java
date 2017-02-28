package se.mah.af6260.gotracker;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new DBHandler();
        serviceConnection = new MyServiceConnection(this);
        serviceIntent = new Intent(this, RunService.class);
//        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
//        Toast.makeText(this, "Step Detector Service bound!", Toast.LENGTH_SHORT).show();
        setFragment(new StartFrag(), false);
    }


    public void bindRunService(){
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        Toast.makeText(this, "Step Detector Service bound!", Toast.LENGTH_SHORT).show();
    }

    public void unbindRunService(){
        if (serviceBound) {
            unbindService(serviceConnection);
            serviceBound = false;
            Log.v("Pedometer", "service unbound");
            Toast.makeText(this, "Step Detector Service unbound!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (serviceBound) {
            unbindService(serviceConnection);
            serviceBound = false;
            Log.v("Pedometer", "service unbound");
            Toast.makeText(this, "Step Detector Service unbound!", Toast.LENGTH_SHORT).show();
        }

        super.onDestroy();
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
        int status = context.getPackageManager().checkPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION,
                context.getPackageName());
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
