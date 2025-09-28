package lift;

import java.util.ArrayList;

public class Monitor {
    private static final int MAX_LOAD = 4;
    private int NBR_FLOORS;

    private int exiting = 0;
    private int floor;
    private boolean doorsOpen = false;
    private int entering = 0;
    private boolean goingUp = true;

    private ArrayList<Person> loadedPersons = new ArrayList<>();
    private ArrayList<Person> waitingPersons = new ArrayList<>();

    public Monitor(int NBR_FLOORS) {
        this.NBR_FLOORS = NBR_FLOORS;
    }

    public synchronized void setCurrentFloor(int floor) {
        this.floor = floor;
        notifyAll();
    }

    // check if person can enter by checking if lift is full
    private synchronized boolean personCanEnter(Person person) {
        System.out.println("Person can enter START -----");
        System.out.println("rightFloor " + Boolean.toString(floor == person.getStartFloor()));
        System.out.println("doorsOpen " + Boolean.toString(doorsOpen));
        System.out.println("isDirectionFit " + Boolean.toString(isDirectionFit(person)));
        return !liftFull() && floor == person.getStartFloor() && doorsOpen && isDirectionFit(person);
    }

    private boolean isDirectionFit(Person person) {
        if (floor == 0 || floor == NBR_FLOORS - 1) {
            return true;
        }
        if (person.getDestinationFloor() > floor && goingUp) {
            return true;
        }
        if (person.getDestinationFloor() < floor && !goingUp) {
            return true;
        }
        return false;
    }

    private synchronized boolean personCanExit(Person person) {
        return floor == person.getDestinationFloor() && doorsOpen;
    }

    public synchronized int getCurrentFloor() {
        return floor;
    }

    // adds person to the waiting list for entering the lift
    public synchronized void addWaitingPerson(Person person) {
        waitingPersons.add(person);
        notifyAll();
    }

    // if person can't enter -> wait, else let oerson enter lift
    public synchronized void enterWhenAllowed(Person person) throws InterruptedException {
        while (!personCanEnter(person)) {
            wait();
        }
        entering++;
        notifyAll();
    }

    public synchronized void exitWhenAllowed(Person person) throws InterruptedException {
        while (!personCanExit(person)) {
            wait();
        }
        exiting++;
        notifyAll();
    }

    // delete person that just entered from list of persons who want's to enter
    public synchronized void entered(Person person) {
        entering--;
        loadedPersons.add(person);
        waitingPersons.remove(person);
        notifyAll();
    }

    public synchronized void exited(Person person) {
        loadedPersons.remove(person);
        notifyAll();
    }

    public synchronized boolean passengerWantToExit() {
        for (Person person : loadedPersons) { // checks all passengers in the lift
            if (personCanExit(person) || exiting > 0) { // if anyone can exit or someone already is exiting
                return true;
            }
        }
        return false;
    }

    public synchronized boolean passengerWantToEnter() {
        for (Person person : waitingPersons) {
            if (personCanEnter(person) || entering > 0) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean isEmpty() {
        return loadedPersons.size() == 0;
    }

    // check if lift is full
    public synchronized boolean liftFull() {
        return loadedPersons.size() >= MAX_LOAD;
    }

    public synchronized boolean personsToServe() {
        return !(waitingPersons.isEmpty() && loadedPersons.isEmpty());
    }

    public int getNextFloor() {
        if (floor == 0) {
            goingUp = true;
        } else if (floor == NBR_FLOORS - 1) {
            goingUp = false;
        }
        return goingUp ? floor + 1 : floor - 1;
    }

    public synchronized void setDoorsOpen(boolean open) {
        doorsOpen = open;
        notifyAll();
    }

    public synchronized void waitForPassengerMove() {
        waitForUnload();
        waitForLoad();
    }

    private void waitForUnload() {
        while (!isEmpty() && passengerWantToExit()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void waitForLoad() {
        while (!liftFull() && passengerWantToEnter()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
