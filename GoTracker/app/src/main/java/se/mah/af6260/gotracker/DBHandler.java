package se.mah.af6260.gotracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Date;
import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 8;
    private static final String DATABASE_NAME = "goTracker.db";
    //TABLES
    public static final String TABLE_SESSION = "session", TABLE_ROUTES = "route";
    //COLUMNS
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ACTIVITY = "_activity";
    public static final String COLUMN_START_YEAR = "_startYear";
    public static final String COLUMN_START_MONTH = "_startMonth";
    public static final String COLUMN_START_DAY = "_startDay";
    public static final String COLUMN_START_TIME = "_startTime";
    public static final String COLUMN_DURATION = "_duration";
    public static final String COLUMN_DISTANCE = "_distance";
    public static final String COLUMN_STEPS = "_steps";
    public static final String COLUMN_AVG_SPEED = "_averageSpeed";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_SESSIONID = "sessionid";
    public static final String COLUMN_COUNT = "count";
    //CREATE STATEMENTS
    private static final String CREATE_TABLE_SESSIONS = "CREATE TABLE " + TABLE_SESSION + "( " +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ACTIVITY + " TEXT, " +
            COLUMN_START_YEAR + " INTEGER, " +
            COLUMN_START_MONTH + " INTEGER, " +
            COLUMN_START_DAY + " INTEGER, " +
            COLUMN_START_TIME + " STRING, " +
            COLUMN_DURATION + " STRING, " +
            COLUMN_DISTANCE + " STRING, " +
            COLUMN_STEPS + " INTEGER, " +
            COLUMN_AVG_SPEED + " DOUBLE " +
            ");";

    private static final String CREATE_TABLE_ROUTES = "CREATE TABLE " + TABLE_ROUTES + "( " +
//            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_COUNT + " INTEGER, " +
            COLUMN_SESSIONID + " INTEGER, " +
            COLUMN_LATITUDE + " DOUBLE, " +
            COLUMN_LONGITUDE + " DOUBLE, " +
            "FOREIGN KEY (" + COLUMN_SESSIONID + ") REFERENCES " + TABLE_SESSION+"("+COLUMN_ID+")"+
            ");";



    public DBHandler (Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SESSIONS);
        db.execSQL(CREATE_TABLE_ROUTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSION);
        onCreate(db);
    }

    public void newSession(Session session) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACTIVITY, session.getActivityType());
        values.put(COLUMN_START_YEAR, session.getStartYear());
        values.put(COLUMN_START_MONTH, session.getStartMonth());
        values.put(COLUMN_START_DAY, session.getStartDay());
        values.put(COLUMN_START_TIME, session.getStartTime());
        values.put(COLUMN_DURATION, session.getDuration());
        values.put(COLUMN_DISTANCE, session.getDistance());
        values.put(COLUMN_STEPS, session.getSteps());
        values.put(COLUMN_AVG_SPEED, session.getAvgSpeed());
        db.insert(TABLE_SESSION, null, values);
        db.close();
        SQLiteDatabase dbRead = getReadableDatabase();
        Cursor cursor = dbRead.rawQuery("SELECT * FROM " + TABLE_SESSION , null);
        int maxID = 0;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            if(maxID < id){
                maxID = id;
            }
        }
        dbRead.close();
        newRoute(maxID, session);
    }

    private void newRoute(int id, Session session){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        ArrayList<LatLng> list = session.getRouteArray();
        for (int i=0; i<list.size()-1; i+=10){
            values.put(COLUMN_SESSIONID, id);
            values.put(COLUMN_LATITUDE, list.get(i).latitude);
            values.put(COLUMN_LONGITUDE, list.get(i).longitude);
        }
        values.put(COLUMN_SESSIONID, id);
        values.put(COLUMN_LATITUDE, list.get(list.size()-1).latitude);
        values.put(COLUMN_LONGITUDE, list.get(list.size()-1).longitude);
        db.insert(TABLE_ROUTES,null,values);
        db.close();
    }

    public ArrayList<Session> getFullSession(int year, int month, int day){
        ArrayList<Session> listSession = new ArrayList<Session>();
        SQLiteDatabase dbRead = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_SESSION + " WHERE " +
                COLUMN_START_YEAR + " = " + year +
                " AND " + COLUMN_START_MONTH + " = " + month +
                " AND " + COLUMN_START_DAY + " = " + day;
        Cursor cursor = dbRead.rawQuery(query, null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            String activity = cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY));
            int startYear = cursor.getInt(cursor.getColumnIndex(COLUMN_START_YEAR));
            int startMonth = cursor.getInt(cursor.getColumnIndex(COLUMN_START_MONTH));
            int startDay = cursor.getInt(cursor.getColumnIndex(COLUMN_START_DAY));
            String startTime = cursor.getString(cursor.getColumnIndex(COLUMN_START_TIME));
            String duration = cursor.getString(cursor.getColumnIndex(COLUMN_DURATION));
            String distance = cursor.getString(cursor.getColumnIndex(COLUMN_DISTANCE));
            int steps = cursor.getInt(cursor.getColumnIndex(COLUMN_STEPS));
            double avgSpeed = cursor.getDouble(cursor.getColumnIndex(COLUMN_AVG_SPEED));
            ArrayList<LatLng> route = getRoute(id);
            listSession.add(new Session(id, activity, startYear, startMonth, startDay, startTime, duration,
                    distance, steps, avgSpeed, route));
        }
        dbRead.close();
        return listSession;
    }

    private ArrayList<LatLng> getRoute(int id){
        ArrayList<LatLng> route = new ArrayList<LatLng>();
        SQLiteDatabase dbRead = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ROUTES + " WHERE " +
                COLUMN_SESSIONID + " = " + id;
        Cursor cursor = dbRead.rawQuery(query, null);
        while(cursor.moveToNext()){
            double longitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE));
            double latitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE));
            route.add(new LatLng(latitude, longitude));
        }
        dbRead.close();
        return route;
    }
}
