package se.mah.af6260.gotracker;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartFrag extends Fragment {

    private Button btnStart, btnSessions, btnMap, btnStartRun;
    private Button btnRun, btnWalk, btnBicycle;
    private TextView tvGps, tvStepDetecter;
    private ImageView ivGps, ivStepdetector;

    public StartFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        btnStart = (Button)view.findViewById(R.id.btnStart);
        btnStartRun = (Button)view.findViewById(R.id.btnStartRun);
        ivGps = (ImageView)view.findViewById(R.id.ivGps);
        ivStepdetector = (ImageView)view.findViewById(R.id.ivStepdetector);

        btnStartRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setFragment(new RunFrag(), true);


                //ivGps.setImageResource(R.drawable.success); Change img to success
                //ivStepdetector.setImageResource(R.drawable.error); Change img to error
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
    public void stepDetectorSuccess(){
        ivStepdetector.setImageResource(R.drawable.error);
    }

}
