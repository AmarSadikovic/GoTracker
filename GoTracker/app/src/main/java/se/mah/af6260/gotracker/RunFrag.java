package se.mah.af6260.gotracker;



import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RunFrag extends Fragment implements OnMapReadyCallback {
    private GoogleMap map;
    private TextView tvSteps;
    private TextView tvTimer;
    private int stepsTaken = 0;
    private Handler handler;
    private Runnable runnable;
    private Stopwatch stopwatch;
    private LatLng startPosition;
    private ArrayList<LatLng> route = new ArrayList<LatLng>();

    public RunFrag() {
        // Required empty public constructor
    }

    public void updateSteps() {
        stepsTaken++;
        tvSteps.setText("Steps taken : " + stepsTaken);
    }

    public void updateTimer(String timer) {
        tvTimer.setText("Time : " + timer);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_run, container, false);
        MapFragment mapFragment = (MapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button btn = (Button) v.findViewById(R.id.btnStopRun);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                stopwatch.stopTimer();
                ((MainActivity) getActivity()).unbindRunService();
                ((MainActivity) getActivity()).setStartFrag();
            }
        });
        tvSteps = (TextView) v.findViewById(R.id.tvSteps);
        tvTimer = (TextView) v.findViewById(R.id.tvTime);
        stopwatch = new Stopwatch();
        stopwatch.startTimer();
        updateUI();
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        startPosition =  ((MainActivity)getActivity()).getLocation(getActivity(), getActivity());
        route.add(startPosition);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(startPosition, 15.0f));
        map.addMarker(new MarkerOptions().position(startPosition).title("My position"));

    }
    private int updateTimer = 0;
    public void updateUI() {
        handler = new Handler();
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                String time = stopwatch.getTime();

                if(updateTimer == 20){
                    updateTimer = 0;
                    LatLng lastPos = route.get(route.size()-1);
                    LatLng newPos = ((MainActivity)getActivity()).getLocation(getActivity(), getActivity());
                    route.add(newPos);
                    map.addPolyline(new PolylineOptions()
                            .add(lastPos, newPos)
                            .width(15)
                            .color(Color.BLUE));
                } else {
                    updateTimer++;
                }
                updateTimer(time);
                updateUI();
            }
        }, 100);
    }
}
