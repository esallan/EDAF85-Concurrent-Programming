package lift;

import java.util.ArrayList;

public class Monitor extends Thread {
    private static final int MAX_LOAD = 4;
    private static final int NBR_FLOORS = 6;

    private int personsInQueue = 0;
    private int exiting = 0;
    private int personsInTheLift = 0;
    private int floor;
    private boolean doorsOpen = false;
    private int entering = 0;
    private boolean goingUp = true;
    private boolean moving;

    private ArrayList<Person> loadedPersons = new ArrayList<>();
    private ArrayList<Person> waitingPersons = new ArrayList<>();

    public void setCurrentFloor(int floor) {
        this.floor = floor;
    }

    // check if person can enter by checking if lift is full
    private boolean personCanEnter(Person person) {
        boolean direction;

        if (person.getStartFloor() == 0 || person.getStartFloor() == 6) {
            direction = true;
        } else {
            direction = this.goingUp == person.isGoingUp();
        }

        return !liftFull() && getCurrentFloor() == person.getStartFloor() && doorsOpen && direction;
    }

    private boolean personCanExit(Person person) {
        return getCurrentFloor() == person.getDestinationFloor() && doorsOpen;

    }

    // adds person to the list with persons that is curently in the lift
    private void enterLift(Person person) {
        loadedPersons.add(person);

    }

    public int getCurrentFloor() {
        return floor;
    }

    // adds person to the waiting list for entering the lift
    public void addWaitingPerson(Person person) {
        waitingPersons.add(person);
    }

    // if person can't enter -> wait, else let oerson enter lift
    public synchronized void enterWhenAllowed(Person person) throws InterruptedException {
        while (!personCanEnter(person)) {
            wait();
        }
        enterLift(person);
        personsInQueue++;
        notifyAll(); // SHOULD I REALLY HAVE THIS?
    }

    public synchronized void exitWhenAllowed(Person person) throws InterruptedException {
        while (!personCanExit(person)) {
            wait();
        }
        exiting++;

    }

    // delete person that just entered from list of persons who want's to enter
    public void entered(Person person) {
        waitingPersons.remove(person);
        personsInQueue--;
        personsInTheLift++;
    }

    public void exited(Person person) {
        loadedPersons.remove(person);
        personsInTheLift--;

    }

    public boolean passengerWantToExit() {
        for (Person person : loadedPersons) { // checks all passengers in the lift
            if (personCanExit(person) || exiting > 0) { // if anyone can exit or someone already is exiting
                return true;
            }
        }
        return false;
    }

    public boolean passengerWantToEnter() {
        for (Person person : waitingPersons) {
            if (personCanEnter(person) || entering > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return personsInTheLift == 0;
    }

    // check if lift is full
    public boolean liftFull() {
        return personsInTheLift >= MAX_LOAD;
    }

    public boolean personsToServe() {
        return !(waitingPersons.isEmpty() && loadedPersons.isEmpty());
    }

    public int getNextFloor() {
        if (floor == 0) {
            goingUp = true;
        } else if (floor == NBR_FLOORS) {
            goingUp = false;
        }
        return goingUp ? floor + 1 : floor - 1;
    }

    public synchronized void toggleMoving() {
        moving = !moving;
    }

    public synchronized void setDoorsOpen(boolean open) {
        doorsOpen = open;
    }

}
