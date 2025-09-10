import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.Semaphore;

import clock.AlarmClockEmulator;
import clock.io.Choice;
import clock.io.ClockData;
import clock.io.ClockInput;
import clock.io.ClockInput.UserInput;
import clock.io.ClockOutput;
import clock.io.TickerThread;

public class ClockMain {
    public static void main(String[] args) throws InterruptedException {
        ClockData clockData = new ClockData();

        ClockInput  in  = clockData.getInput();
        ClockOutput out = clockData.getOutput();

        clockData.setTimeToNow(); 


        Semaphore semaphore = new Semaphore(0);

        Thread tickerThread = new TickerThread(clockData);

        tickerThread.start();


        while (true) {
            semaphore.acquire();

            UserInput userInput = in.getUserInput();
            Choice choice = userInput.choice();
            int h = userInput.hours();
            int m = userInput.minutes();
            int s = userInput.seconds();

            if (clockData.alarmIsActive()) {
                clockData.toggleAlarm();
            }

            switch(choice){
                case SET_TIME:
                    clockData.setAlarmTime(h, m, s);
                    break;
                case SET_ALARM:
                    clockData.setAlarmTime(h, m, s);
                    break;
                case TOGGLE_ALARM:
                    clockData.toggleAlarm();
                    break;

            }

         //   System.out.println("choice=" + c + " h=" + h + " m=" + m + " s=" + s);
        }
    }
}
