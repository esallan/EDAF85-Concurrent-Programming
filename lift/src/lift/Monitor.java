package lift;

import java.util.ArrayList;

public class Monitor extends Thread {
    private static final int MAX_LOAD = 4;
    private static final int NBR_FLOORS = 5;

    private int currentFloor;
    private boolean doorsOpen = false;

    private int entering = 0; // persons entering right now
    private int exiting = 0; // persons exiting right now
    private int personsInTheLift = 0; // persons in the lift right now

    private boolean goingUp = true;
    private boolean moving;

    private ArrayList<Person> loadedPersons = new ArrayList<>();
    private ArrayList<Person> waitingPersons = new ArrayList<>();

    public void setCurrentFloor(int floor) {
        this.currentFloor = floor;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public boolean isDoorsOpen() {
        return doorsOpen;
    }

    public boolean liftFull() {
        return personsInTheLift >= MAX_LOAD;
    }

    public boolean isEmpty() {
        return personsInTheLift == 0;
    }

    public boolean personsToServe() {
        return !(waitingPersons.isEmpty() && loadedPersons.isEmpty());
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
    private synchronized void enterLift(Person person) {
        loadedPersons.add(person);
        personsInTheLift++;
        waitingPersons.remove(person);

    }

    public synchronized void exitLift(Person person) {
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
        personsInTheLift++;
    }

    public void exited(Person person) {
        loadedPersons.remove(person);
        personsInTheLift--;

    }

    public int getNextFloor() {
        if (!personsToServe())
            return currentFloor;

        if (currentFloor == 0)
            goingUp = true;
        else if (currentFloor == NBR_FLOORS)
            goingUp = false;

        int next = currentFloor;

        if (goingUp) {
            for (int floor = currentFloor + 1; floor <= NBR_FLOORS; floor++) {
                if (someoneWantsOnOrOff(floor)) {
                    next = floor;
                    break;
                }
            }
        } else {
            for (int floor = currentFloor - 1; floor >= 0; floor--) {
                if (someoneWantsOnOrOff(floor)) {
                    next = floor;
                    break;
                }
            }
        }

        if (next == currentFloor) {
            goingUp = !goingUp;
            return getNextFloor();
        }

        return next;
    }

    public void updateDoors() {
        doorsOpen = someoneWantsOnOrOff(currentFloor);
    }

    private boolean someoneWantsOnOrOff(int floor) {
        for (Person person : loadedPersons) {
            if (person.getDestinationFloor() == floor) {
                return true;
            }
        }

        for (Person person : waitingPersons) {
            if (person.getStartFloor() == floor) {
                return true;
            }
        }

        return false;
    }

    public synchronized void toggleMoving() {
        moving = !moving;
    }

}
