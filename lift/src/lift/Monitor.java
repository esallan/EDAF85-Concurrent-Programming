package lift;

import java.util.ArrayList;

public class Monitor {
    private static final int MAX_LOAD = 4;

    private int entering = 0;
    private int load = 0;

    private ArrayList<Person> loadedPersons = new ArrayList<>();
    private ArrayList<Person> waitingPersons = new ArrayList<>();

    // check if lift is full
    private boolean liftFull() {
        return load >= MAX_LOAD;
    }

    // check if person can enter by checking if lift is full
    private boolean personCanEnter(Person person) {
        return !liftFull();
    }

    // adds person to the waiting list for entering the lift
    public void addWaitingPerson(Person person) {
        waitingPersons.add(person);
    }

    // adds person to the list with persons that is curently in the lift
    private void enterLift(Person person) {
        loadedPersons.add(person);

    }

    // if person can't enter -> wait, else let oerson enter lift
    public void enterWhenAllowed(Person person) throws InterruptedException {
        while (!personCanEnter(person)) {
            wait();
        }
        enterLift(person);
        entering++;
    }

    public void exitWhenAllowed(Person person) {
    }

    // delete person that just entered from list of persons who want's to enter
    public void entered(Person person) {

    }

    public void exited(Person person) {

    }
}
