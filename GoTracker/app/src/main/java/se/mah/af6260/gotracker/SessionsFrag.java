package se.mah.af6260.gotracker;


import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class SessionsFrag extends Fragment {
    private CalendarView cv;
    private DBHandler dbHandler;
    private SimpleCursorAdapter dataAdapter;
    private TextView tvStartTime, tvDuration;
    private ListView listView;
    private View v;

    public SessionsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_sessions, container, false);
        cv = (CalendarView)v.findViewById(R.id.calendarView);
        dbHandler = new DBHandler(getActivity(), null, null, 1);
        tvStartTime = (TextView)getActivity().findViewById(R.id.tvStartTime);
        tvDuration = (TextView)getActivity().findViewById(R.id.tvDuration);
        listView = (ListView)getActivity().findViewById(R.id.lvSessionInfo);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int years, int months, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(years, months, dayOfMonth);
                java.sql.Date sqlDate2 = new java.sql.Date(cal.getTimeInMillis());
                displayListView(dbHandler.getSessionsByDate(sqlDate2));
                //Låt stå för tillfället plz, för testing purposes
//                Cursor selectedDateCursor = dbHandler.getSessionsByDate(sqlDate2);
//                selectedDateCursor.moveToFirst();
//                try {
//                    while (selectedDateCursor.moveToNext()) {
//                        System.out.println(selectedDateCursor.getString(selectedDateCursor.getColumnIndex("_activity")));
//                    }
//                } finally {
//                    selectedDateCursor.close();
//                }

//                displayListView(selectedDateCursor);
            }
        });
        return v;
    }

    private void displayListView(Cursor sessionInfo) {

        String[] columns = new String[]{
                dbHandler.COLUMN_ACTIVITY,
                dbHandler.COLUMN_START_TIME,
                dbHandler.COLUMN_DURATION,
        };
        int[] to = new int[]{
                R.id.ivActivity,
                R.id.tvStartTime,
                R.id.tvDuration,
        };
        dataAdapter = new SimpleCursorAdapter(
                getActivity(), R.layout.cursor_adapter_sessions,
                sessionInfo,
                columns,
                to,
                0);
        dataAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.ivActivity) {
                    ImageView IV = (ImageView) view;
                    int resID = getActivity().getApplicationContext().getResources().getIdentifier(cursor.getString(columnIndex), "drawable", getActivity().getApplicationContext().getPackageName());
                    IV.setImageDrawable(getActivity().getApplicationContext().getResources().getDrawable(resID));

                    return true;
                }
                return false;
            }
        });
        listView = (ListView) v.findViewById(R.id.lvSessionInfo);
        listView.setAdapter(dataAdapter);
    }

}
