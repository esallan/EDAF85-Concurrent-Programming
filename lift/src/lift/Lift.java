package lift;

public class Lift extends Thread {
    LiftView view;
    Monitor monitor;
    Person[] persons;

    public Lift(LiftView liftView, Monitor monitor, Person[] persons) {
        this.view = liftView;
        this.monitor = monitor;
        this.persons = persons;
        monitor.setCurrentFloor(0);
        this.view.openDoors(0);
    }

    public void run() {
        while (true) {
            monitor.waitForPassengerMove();
            if (monitor.personsToServe()) {
                moveToNextFloor();
            }
        }
    }

    private void moveToNextFloor() {
        int currentFloor = monitor.getCurrentFloor();
        int nextFloor = monitor.getNextFloor();

        monitor.setDoorsOpen(false);
        view.closeDoors();
        view.moveLift(currentFloor, nextFloor);
        view.openDoors(nextFloor);
        monitor.setDoorsOpen(true);
        monitor.setCurrentFloor(nextFloor);

    }
}
