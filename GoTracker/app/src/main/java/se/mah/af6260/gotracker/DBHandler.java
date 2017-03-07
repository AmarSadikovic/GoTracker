package se.mah.af6260.gotracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "goTracker.db";
    //TABLES
    public static final String TABLE_SESSION = "session";
    //COLUMNS
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ACTIVITY = "_activity";
    public static final String COLUMN_START_DATE = "_startDate";
    public static final String COLUMN_START_TIME = "_startTime";
    public static final String COLUMN_DURATION = "_duration";
    public static final String COLUMN_DISTANCE = "_distance";
    public static final String COLUMN_STEPS = "_steps";
    public static final String COLUMN_AVG_SPEED = "_averageSpeed";
    public static final String COLUMN_STEPS_PER_SECOND = "_stepsPerSecond";
    //CREATE STATEMENT
    private static final String CREATE_TABLE_SESSIONS = "CREATE TABLE " + TABLE_SESSION + "( " +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ACTIVITY + " TEXT, " +
            COLUMN_START_DATE + " DATE, " +
            COLUMN_START_TIME + " TIME, " +
            COLUMN_DURATION + " INTEGER, " +
            COLUMN_DISTANCE + " INTEGER, " +
            COLUMN_STEPS + " INTEGER, " +
            COLUMN_AVG_SPEED + " DOUBLE, " +
            COLUMN_STEPS_PER_SECOND + " DOUBLE" +
            ");";



    public DBHandler (Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SESSIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSION);
        onCreate(db);
    }

    public void newSession(Session session) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO "+TABLE_SESSION+" (" + COLUMN_ACTIVITY+","+COLUMN_START_DATE+","+COLUMN_START_TIME+","+COLUMN_DURATION+","+COLUMN_DISTANCE+","+COLUMN_STEPS+","+COLUMN_AVG_SPEED+","+COLUMN_STEPS_PER_SECOND+") VALUES (\"" +
                session.getActivity() + "\"" +
                ",\"" + session.getStartDate() + "\"" +
                ",\"" + session.getStartTime() + "\"" +
                "," + session.getDuration() +
                "," + session.getDistance() +
                "," + session.getSteps() +
                "," + session.getAvgSpeed() +
                "," + session.getStepsPerSecond() +
                ")");
        db.close();
    }

    public Cursor getSessionsByDate(Date date){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_SESSION + " WHERE " + COLUMN_START_DATE + " = " + date;
        //Cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);
        db.close();
        return c;
    }
}
