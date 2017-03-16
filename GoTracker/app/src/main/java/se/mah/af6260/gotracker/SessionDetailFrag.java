package se.mah.af6260.gotracker;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SessionDetailFrag extends Fragment implements OnMapReadyCallback {
    private TextView tvSteps;
    private TextView tvTimer;
    private TextView tvDistance;
    private TextView tvDate;
    private TextView tvActivity;
    private TextView tvStartime;
    private TextView tvAverageSpeed;
    private Session session;
    private GoogleMap map;
    private ArrayList<LatLng> route;
    public SessionDetailFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_session_detail, container, false);
        MapFragment mapFragment = (MapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        tvSteps = (TextView) v.findViewById(R.id.tvStepsSessionDetail);
        tvTimer = (TextView) v.findViewById(R.id.tvTimeSessionDetail);
        tvDistance = (TextView) v.findViewById(R.id.tvDistanceSessionDetail);
        tvDate = (TextView) v.findViewById(R.id.tvDateSessionDetail);
        tvStartime = (TextView) v.findViewById(R.id.tvStartTimeSessionDetail);
        tvActivity = (TextView) v.findViewById(R.id.tvActivitySessionDetail);
        tvAverageSpeed = (TextView) v.findViewById(R.id.tvAverageSpeedSessionDetail);
        session = ((MainActivity)getActivity()).getSelectedSession();
        tvActivity.setText("Activity : " + session.getActivityType());
        tvSteps.setText("Steps taken : " + session.getSteps());
        tvTimer.setText("Time : " + session.getDuration());
        tvDistance.setText("Distance : " + session.getDistance() + "m");
        tvDate.setText("Date : " + session.getStartYear() + "-" + session.getStartMonth() + "-" + session.getStartDay());
        tvStartime.setText("Start time : " + session.getStartTime());
        tvAverageSpeed.setText("Average Speed : " + session.getAvgSpeed());
        route = session.getRouteArray();
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        if(route.size() > 0) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(route.get(0), 15.0f));
        }
        for(int i = 0; i<route.size()-1; i++){

            map.addPolyline(new PolylineOptions()
                    .add(route.get(i), route.get(i+1))
                    .width(15)
                    .color(Color.BLUE));
        }
    }

}
