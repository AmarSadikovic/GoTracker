package se.mah.af6260.gotracker;

import android.database.Cursor;

import java.util.Calendar;

/**
 * Created by Koffe on 2017-03-07.
 */

public class TestDB {

    private DBHandler db;

    public TestDB (DBHandler db){
        this.db = db;
        testInsert();
    }

    private void testInsert(){
//        Calendar calendar = Calendar.getInstance();
//        java.util.Date utilDate = calendar.getTime();
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
        java.sql.Time sqlTime = new java.sql.Time(utilDate.getTime());
//        Calendar calendar = Calendar.getInstance();
//        java.sql.Date ourJavaDateObject = new java.sql.Date(calendar.getTime().getTime());
        Session newSession = new Session ("Running", sqlDate, sqlTime, 60, 200, 7000, 10, 3);
        System.out.println("StartDate: " + newSession.getStartDate() + " StartTime: " + newSession.getStartTime());
        System.out.println("StartDate: " + newSession.getStartDate() + " StartTime: " + newSession.getStartTime());
        System.out.println("StartDate: " + newSession.getStartDate() + " StartTime: " + newSession.getStartTime());
        System.out.println("StartDate: " + newSession.getStartDate() + " StartTime: " + newSession.getStartTime());
        System.out.println("StartDate: " + newSession.getStartDate() + " StartTime: " + newSession.getStartTime());
        System.out.println("StartDate: " + newSession.getStartDate() + " StartTime: " + newSession.getStartTime());
        System.out.println("StartDate: " + newSession.getStartDate() + " StartTime: " + newSession.getStartTime());

        db.newSession(newSession);
//        Cursor result = db.getSessionsByDate(sqlDate);
//        result.moveToFirst();
//        System.out.println(result.toString());
    }
}
