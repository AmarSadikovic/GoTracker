package se.mah.af6260.gotracker;


import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class SessionsFrag extends Fragment  {
    private CalendarView cv;
    private DBHandler dbHandler;
    private SimpleCursorAdapter dataAdapter;
    private TextView tvStartTime, tvDuration;
    private ListView listView;
    private View v;
    private ArrayList<Session> listSessions;

    public SessionsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_sessions, container, false);
        cv = (CalendarView)v.findViewById(R.id.calendarView);
        dbHandler = new DBHandler(getActivity(), null, null, 1);
        tvStartTime = (TextView)v.findViewById(R.id.tvStartTime);
        tvDuration = (TextView)v.findViewById(R.id.tvDuration);
        listView = (ListView)v.findViewById(R.id.lvSessionInfo);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int years, int months, int dayOfMonth) {
                Log.v("TO DATABASE ", years + " MONTH " + months+1 + " DAY " + dayOfMonth);
                listSessions = dbHandler.getFullSession(years, months+1, dayOfMonth);
                listView.setAdapter(new SessionAdapter(getActivity(), listSessions));
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ((MainActivity)getActivity()).setSelectedSession(listSessions.get(position));
                ((MainActivity)getActivity()).setFragment(new SessionDetailFrag(), true);
            }
        });
        return v;
    }

}
