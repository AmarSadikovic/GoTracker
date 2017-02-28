package se.mah.af6260.gotracker;


import android.os.Bundle;
import android.app.Fragment;
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

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class RunFrag extends Fragment implements OnMapReadyCallback {
    private GoogleMap map;
    private TextView tvSteps;
    private int stepsTaken = 0;

    public RunFrag() {
        // Required empty public constructor
    }

    public void updateSteps(){
        stepsTaken++;
        tvSteps.setText("Steps taken : " + stepsTaken);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_run, container, false);
        MapFragment mapFragment = (MapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button btn = (Button)v.findViewById(R.id.btnStopRun);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).unbindRunService();
                ((MainActivity)getActivity()).setStartFrag();
            }
        });
        tvSteps = (TextView)v.findViewById(R.id.tvSteps);
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        LatLng latLng =  ((MainActivity)getActivity()).getLocation(getActivity(), getActivity());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.0f));
        map.addMarker(new MarkerOptions().position(latLng).title("My position"));
    }
}
