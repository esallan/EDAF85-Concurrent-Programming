
import lift.Monitor;
import lift.Person;
import lift.LiftView;
import lift.Lift;

public class Simulation {
    private static final int MAX_PASSENGERS = 20;
    private static final int NBR_FLOORS = 7;

    public static void main(String[] args) {
        LiftView view = new LiftView(NBR_FLOORS, MAX_PASSENGERS);
        Monitor monitor = new Monitor();

        Person[] persons = new Person[MAX_PASSENGERS];

        for (int i = 0; i < MAX_PASSENGERS; i++) {
            Person person = new Person(view.createPassenger(), monitor);
            persons[i] = person;
            person.start();
        }

        Lift lift = new Lift(); // ska ta flera parametrar men vill inte det av någon anledning??
        lift.start();

    }

}

/*
 * final int NBR_FLOORS = 7, MAX_PASSENGERS = 4;
 * 
 * LiftView view = new LiftView(NBR_FLOORS, MAX_PASSENGERS);
 * Passenger pass = view.createPassenger();
 * int fromFloor = pass.getStartFloor();
 * int toFloor = pass.getDestinationFloor();
 * 
 * pass.begin(); // walk in (from left)
 * if (fromFloor != 0) {
 * view.moveLift(0, fromFloor);
 * }
 * view.openDoors(fromFloor);
 * pass.enterLift(); // step inside
 * 
 * view.closeDoors();
 * view.moveLift(fromFloor, toFloor); // ride lift
 * view.openDoors(toFloor);
 * 
 * pass.exitLift(); // leave lift
 * pass.end(); // walk out (to the right)
 * }
 */