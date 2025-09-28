
import lift.Monitor;
import lift.Person;
import lift.LiftView;
import lift.Lift;

public class Simulation {
    private static final int NBR_PERSONS = 20;
    private static final int NBR_FLOORS = 7;

    public static void main(String[] args) {
        LiftView view = new LiftView(NBR_FLOORS, NBR_PERSONS);
        Monitor monitor = new Monitor(NBR_FLOORS);

        Person[] persons = new Person[NBR_PERSONS];

        for (int i = 0; i < NBR_PERSONS; i++) {
            Person person = new Person(view.createPassenger(), monitor);
            persons[i] = person;
            person.start();
        }

        Lift lift = new Lift(view, monitor, persons);
        lift.start();

        while (true) {
            for (int i = 0; i < NBR_PERSONS; i++) {
                if (!persons[i].isAlive()) {
                    persons[i] = new Person(view.createPassenger(), monitor);
                    persons[i].start();
                }
            }
        }

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