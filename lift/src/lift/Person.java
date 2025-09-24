package lift;

import java.util.Random;

//each Person is a Passenger that needs to have a Monitor too  

public class Person extends Thread {
    private Passenger passenger;
    private Monitor monitor;
    private boolean goingUp;

    public Person(Passenger passenger, Monitor monitor) {
        this.passenger = passenger;
        this.monitor = monitor;
        goingUp = passenger.getStartFloor() - passenger.getDestinationFloor() > 0 ? true : false;
    }

    @Override
    public void run() { // will be called by start()
        // should be delayed "delay the animate passengers walk"
        try {
            delay(45);
            begin();
            // should enter if allowed
            waitAndEnter();
            // should exit if allowed
            waitAndExit();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void delay(int maxDelaySeconds) throws InterruptedException {
        Random rand = new Random();
        int timeDelayed = rand.nextInt(maxDelaySeconds);
        Thread.sleep(timeDelayed * 1000); // sleep takes milliseconds, thats why * 1000
    }

    private void begin() {
        passenger.begin();
        monitor.addWaitingPerson(this);
    }

    private void waitAndEnter() throws InterruptedException {
        // passenger should enter when allowed - monitor should keep track of that
        monitor.enterWhenAllowed(this);
        // passenger enters lift
        passenger.enterLift();
        // monitor should register that passenger entered
        monitor.entered(this);
    }

    private void waitAndExit() {
        // monitor should keep track of when passenger can exit
        monitor.exitWhenAllowed(this);
        passenger.exitLift();
        // monitor should register when person, passenger, has left
        monitor.exited(this);
    }

}