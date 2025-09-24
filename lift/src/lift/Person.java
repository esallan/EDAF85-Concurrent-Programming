package lift;

import java.util.Random;

import javax.management.monitor.Monitor;
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
        // should enter if allowed
        // should exit if allowed
    }

    private void delay(int maxDelaySeconds) {
        Random rand = new Random();
        int timeDelayed = rand.nextInt(maxDelaySeconds);

    }

    private void begin() {
        passenger.begin();
        // have to add wainting person
    }

    private void waitAndEnter() {
        // passenger should enter when allowed - monitor should keep track of that
        passenger.enterLift();
        // monitor should register that passenger entered
    }

    private void waitAndExit() {
        // monitor should keep track of when passenger can exit
        passenger.exitLift();
        // monitor should register when person, passenger, has left

    }

}
