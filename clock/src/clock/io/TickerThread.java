package clock.io;

public class TickerThread extends Thread{
    private ClockData clockData;
    private final int THREAD_DELAY = 500; //checks two times per second
    private final int TICK_INTERVAL = 1000;

    public TickerThread(ClockData clockData) {
		this.clockData = clockData;
	}


	@Override
public void run() {
    long start = System.currentTimeMillis();

    while(true) {
        long now = System.currentTimeMillis();
        long timeDifference = now - start;
        
        if(timeDifference >= TICK_INTERVAL) {
            clockData.clockTick();
            
            if(clockData.alarmIsActive()) {
                clockData.soundAlarm();
            }
            
            start = now; 
            
            try {
                Thread.sleep(THREAD_DELAY);  
            } catch (InterruptedException e) {
                e.printStackTrace();
            	}
        	}
    	}
	}
}
