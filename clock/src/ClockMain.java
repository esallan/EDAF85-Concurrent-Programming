// removed unused imports
import java.util.concurrent.Semaphore;

import clock.io.Choice;
import clock.io.ClockData;
import clock.io.ClockInput;
import clock.io.ClockInput.UserInput;
import clock.io.TickerThread;

public class ClockMain {
    public static void main(String[] args) throws InterruptedException {
        ClockData clockData = new ClockData();

        ClockInput in  = clockData.getInput();

        clockData.setTimeToNow(); 


        Semaphore semaphore = in.getSemaphore();

        Thread tickerThread = new TickerThread(clockData);

        tickerThread.start();


        while (true) {
            semaphore.acquire(); //wait for user input 

            UserInput userInput = in.getUserInput();
            Choice choice = userInput.choice();
            int h = userInput.hours();
            int m = userInput.minutes();
            int s = userInput.seconds();

            switch(choice){
                case SET_TIME:
                    clockData.setClockTime(h, m, s);
                    break;
                case SET_ALARM:
                    clockData.setAlarmTime(h, m, s);
                    break;
                case TOGGLE_ALARM:
                    clockData.toggleAlarm();
                    break;

            }

        }
    }
}
