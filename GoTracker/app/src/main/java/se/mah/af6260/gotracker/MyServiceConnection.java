package se.mah.af6260.gotracker;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by Koffe on 2017-02-27.
 */

public class MyServiceConnection implements ServiceConnection {

    private MainActivity main;

    public MyServiceConnection(MainActivity main){
        this.main = main;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {
        RunService.LocalBinder binder = (RunService.LocalBinder) service;
        main.runService = binder.getService();
        main.serviceBound = true;
        main.runService.setListenerActivity(main);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        main.serviceBound = false;
    }
}
