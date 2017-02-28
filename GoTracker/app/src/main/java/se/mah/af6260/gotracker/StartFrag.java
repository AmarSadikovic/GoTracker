package se.mah.af6260.gotracker;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartFrag extends Fragment {

    private Button btnStart, btnSessions, btnMap, btnStartRun;
    private Button btnRun, btnWalk, btnBicycle;
    private TextView tvGps, tvStepDetecter;


    public StartFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        btnStart = (Button)view.findViewById(R.id.btnStart);
        btnStartRun = (Button)view.findViewById(R.id.btnStartRun);
        btnStartRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setFragment(new RunFrag(), true);
            }
        });
        btnSessions = (Button)view.findViewById(R.id.btnSessions);
        btnMap = (Button)view.findViewById(R.id.btnMap);

        btnRun = (Button)view.findViewById(R.id.btnRun);
        btnWalk = (Button)view.findViewById(R.id.btnWalk);
        btnBicycle = (Button)view.findViewById(R.id.btnBicycle);

        tvGps = (TextView)view.findViewById(R.id.tvGps);
        tvStepDetecter = (TextView)view.findViewById(R.id.tvStepDetecter);
        return view;
    }

}
