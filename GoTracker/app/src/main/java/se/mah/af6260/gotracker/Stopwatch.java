package se.mah.af6260.gotracker;

import android.util.Log;

/**
 * Created by oskar on 2017-03-01.
 */

public class Stopwatch {
    private long startNanoSeconds;
    private boolean isRunning = false;
    private String seconds, minutes, hours, milliseconds;

    public void startTimer(){
        startNanoSeconds = System.nanoTime();
        isRunning = true;
    }

    public long getStartTime(){
        return startNanoSeconds;
    }

    public String getTime(){
        if(isRunning) {
            long currentNanoSeconds = System.nanoTime() - startNanoSeconds ;
            String format = updateTimer(currentNanoSeconds);
            return format;
        }
        return "00:00:00.00";
    }


    public void stopTimer(){
        isRunning = false;
    }

    private String updateTimer (long time){
        long milli = (long)(time/100000000);
        long secs = (long)(time/1000000000);
        long mins = (long)((time/1000000000)/60);
        long hrs = (long)(((time/1000000000)/60)/60);

		/* Convert the seconds to String
		 * and format to ensure it has
		 * a leading zero when required
		 */
        secs = secs % 60;
        seconds=String.valueOf(secs);
        if(secs == 0){
            seconds = "00";
        }
        if(secs <10 && secs > 0){
            seconds = "0"+seconds;
        }

		/* Convert the minutes to String and format the String */

        mins = mins % 60;
        minutes=String.valueOf(mins);
        if(mins == 0){
            minutes = "00";
        }
        if(mins <10 && mins > 0){
            minutes = "0"+minutes;
        }

    	/* Convert the hours to String and format the String */

        hours=String.valueOf(hrs);
        if(hrs == 0){
            hours = "00";
        }
        if(hrs <10 && hrs > 0){
            hours = "0"+hours;
        }

    	/* Although we are not using milliseconds on the timer in this example
    	 * I included the code in the event that you wanted to include it on your own
    	 */
        milliseconds = String.valueOf((long)milli);
        if(milliseconds.length()==2){
            milliseconds = "0"+milliseconds;
        }
        if(milliseconds.length()<=1){
            milliseconds = "00";
        }
        milliseconds = milliseconds.substring(milliseconds.length()-1);

        return hours + ":" + minutes + ":" + seconds + "." + milliseconds;

    }
}
