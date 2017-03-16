package se.mah.af6260.gotracker;



import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class RunFrag extends Fragment implements OnMapReadyCallback {
    private GoogleMap map;
    private TextView tvSteps;
    private TextView tvTimer;
    private TextView tvDistance;
    private int stepsTaken = 0;
    private Handler handler;
    private Runnable runnable;
    private Stopwatch stopwatch;
    private LatLng startPosition;
    private ArrayList<LatLng> route = new ArrayList<LatLng>();
    private float distanceInMeters = 0;
    private Marker mark;
    private Button btnStartStop;
    private Boolean isStarted = false;
    private String startTime;
    private String duration, distanceString;
    private double avgSpeed = 1337;

    public RunFrag() {
        // Required empty public constructor
    }

    public void updateSteps() {
        if(isStarted) {
            stepsTaken++;
            tvSteps.setText("Steps taken : " + stepsTaken);
            Calendar cal = Calendar.getInstance();


        }
    }

    public void updateTimer(String timer) {
        duration = timer;
        tvTimer.setText("Time : " + timer);
    }

    public void updateDistance(float distance){
        distanceString = String.format("%.2f", distance);
        tvDistance.setText("Distance : " + distanceString + "m");
    }

    public void updateMap(LatLng position){
        LatLng newPos = position;
        mark.setPosition(newPos);
        if(isStarted) {
            LatLng lastPos = route.get(route.size()-1);
            route.add(newPos);
            map.addPolyline(new PolylineOptions()
                    .add(lastPos, newPos)
                    .width(15)
                    .color(Color.BLUE));
            float[] results = new float[1];
            Location.distanceBetween(lastPos.latitude, lastPos.longitude, newPos.latitude, newPos.longitude, results);
            distanceInMeters += results[0];
            updateDistance(distanceInMeters);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_run, container, false);
        MapFragment mapFragment = (MapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tvSteps = (TextView) v.findViewById(R.id.tvSteps);
        if(((MainActivity) getActivity()).getActivityType().equals("cycling")){
            tvSteps.setText("No Stepdetector for cycling");
        }
        tvTimer = (TextView) v.findViewById(R.id.tvTime);
        tvDistance = (TextView) v.findViewById(R.id.tvDistance);
        TextView activity = (TextView)v.findViewById(R.id.tvActivity);
        activity.setText("Activity: " + ((MainActivity) getActivity()).getActivityType());

        btnStartStop = (Button) v.findViewById(R.id.btnStopRun);
        btnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isStarted){
                    stopwatch = new Stopwatch();
                    stopwatch.startTimer();
                    Calendar cal = Calendar.getInstance();
                    startTime = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
//                    startTimeMillis = System.currentTimeMillis();
                    updateUI();
                    startPosition =  ((MainActivity)getActivity()).getLocation();
                    route.add(startPosition);
                    btnStartStop.setText("STOP RUN");
                    isStarted = true;
                }else if(isStarted){
                    handler.removeCallbacks(runnable);
                    stopwatch.stopTimer();
                    saveResultToDatabase();
                    ((MainActivity) getActivity()).unbindRunService();
                    ((MainActivity) getActivity()).setStartFrag();
                    btnStartStop.setText("START RUN");
                    isStarted = false;
                }
            }
        });

        return v;
    }

    private void saveResultToDatabase() {
        DBHandler dbHandler = ((MainActivity)getActivity()).getDBReference();
        LocalDate to = new LocalDate(System.currentTimeMillis());
        Log.v("TO DATABASE ", to.getYear() + " MONTH " + to.getMonthOfYear() + " DAY " + to.getDayOfMonth());
        dbHandler.newSession(new Session(((MainActivity) getActivity()).getActivityType(),
                to.getYear(), to.getMonthOfYear(), to.getDayOfMonth(), startTime, duration, distanceString, stepsTaken, avgSpeed, route));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        startPosition =  ((MainActivity)getActivity()).getLocationMapFrag(getActivity(), getActivity());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(startPosition, 15.0f));
        mark = map.addMarker(new MarkerOptions().position(startPosition).title("My position").icon(BitmapDescriptorFactory.fromResource(R.drawable.mapicon)));

      //  LatLng homePos = new LatLng(55.6910332, 13.1791068);


    }

    public void updateUI() {
        handler = new Handler();
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                String time = stopwatch.getTime();
                updateTimer(time);
                updateUI();
            }
        }, 100);
    }
}
