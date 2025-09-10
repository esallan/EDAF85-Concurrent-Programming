package clock.io;

import java.time.LocalTime;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import clock.AlarmClockEmulator;

public class ClockData {
    private AlarmClockEmulator emulator;
    private ClockInput input;
    private ClockOutput output;
    private int hAlarm = 0;
    private int mAlarm = 0;
    private int sAlarm = 0;
    private int hTime = 0;
    private int mTime = 0;
    private int sTime = 0;
    private final int MAX_NBR_BEEPS = 20;
    private final Lock mutex;

    private boolean alarmSet;
    // Returns the input interface for the clock hardware
    // Returns the output interface for the clock hardware
    // Converts hours, minutes, and seconds to total seconds
    // Converts total seconds to an array of hours, minutes, and seconds
    // Sets the clock time to the current system time


    public ClockData(){
        this.emulator = new AlarmClockEmulator();
        this.input = emulator.getInput();
        this.output = emulator.getOutput();
        this.alarmSet = false;
        mutex = new ReentrantLock();
    }
    
    // Returns the input interface for the clock hardware
    //Doesn't udnerstand completly why
    public ClockInput getInput(){
        return input;
    }

        // Returns the output interface for the clock hardware
        //Doesn't udnerstand completly why
    public ClockOutput getOutput(){
        return output;
    }

    private int toSeconds(int h, int m, int s) {
        int totSeconds = h * 3600 + m * 60 + s;
        return totSeconds;
    }

    // Converts total seconds to an array of hours, minutes, and seconds
    private int[] toClockFormat(int seconds) {
		int hh = (int) (seconds / 3600);
		int mm = ((int) (seconds / 60)) % 60;
		int ss = seconds % 60;
		
		int[] hms = {hh, mm, ss};
		
		return hms;
	}

    
    // Sets the clock time to the current system time
    public void setTimeToNow(){
        LocalTime timeNow = LocalTime.now();
         hTime = timeNow.getHour();
         mTime = timeNow.getMinute();
         sTime = timeNow.getSecond();
      

    }

    public void setAlarmTime(int hour, int minutes, int seconds){
        mutex.lock();
        hAlarm = hour;
        mAlarm = minutes;
        sAlarm = seconds;
        alarmSet = true;
        mutex.unlock();
        
        output.setAlarmIndicator(alarmSet); //alarmSet == true -> this should work beacuse the methid takes a boolean
        
    }

    // Sets the current clock time and updates the display
    public void setClockTime(int hour, int minutes, int seconds){
        mutex.lock();
        hTime = hour;
        mTime = minutes;
        sTime = seconds;
        mutex.unlock();
        output.displayTime(hTime, mTime, sTime);
    }

    // Advances the clock by one second and updates the display
    public void clockTick() {
        mutex.lock();
        int totSeconds = toSeconds(hTime, mTime, sTime); // convert current time to total seconds
        totSeconds++; // increment by one second
        int[] hms = toClockFormat(totSeconds); // convert back to hours, minutes, seconds
        hTime = hms[0];
        mTime = hms[1];
        sTime = hms[2];
        mutex.unlock();
 
        output.displayTime(hTime, mTime, sTime); // update the display
    }

    // Triggers the alarm output and disables the alarm after a set number of beeps
    public void soundAlarm(){
       mutex.lock();
        output.alarm(); // sound the alarm
        // if the alarm has sounded enough times, turn off the alarm
        if (toSeconds(hAlarm, mAlarm, sAlarm) + MAX_NBR_BEEPS <= toSeconds(hTime, mTime, sTime)) {
            alarmSet = false;
            output.setAlarmIndicator(false);
        }

        mutex.unlock();

    }

    // Enables or disables the alarm and updates the alarm indicator
    public void toggleAlarm() {
        alarmSet = !alarmSet;
        output.setAlarmIndicator(alarmSet);
    }

    // Returns true if the alarm is set and the current time is at or past the alarm time
    public boolean alarmIsActive(){
        return alarmSet && toSeconds(hTime, mTime, sTime) >= toSeconds(hAlarm, mAlarm, sAlarm);
        
    }
}

