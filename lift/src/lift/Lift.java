package lift;

public class Lift extends Thread {
    LiftView view;
    Monitor monitor;
    Person[] persons;
    private static final int WAITING_TIME = 100;

    public Lift(LiftView liftView, Monitor monitor, Person[] persons) {
        this.view = liftView;
        this.monitor = monitor;
        this.persons = persons;
        monitor.setCurrentFloor(0);
        this.view.openDoors(0);
    }

    public void run() {
        while (true) {
            updatePassengers();
            if (monitor.personsToServe()) {
                moveToNextFloor();
            }
        }
    }

    private void updatePassengers() {
        unloadPassengers();
        loadPassengers();
    }

    private void unloadPassengers() {
        while (!monitor.isEmpty() && monitor.passengerWantToExit()) {
            try {
                wait(WAITING_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadPassengers() {
        while (!monitor.liftFull() && monitor.passengerWantToEnter()) {
            try {
                wait(WAITING_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void moveToNextFloor() {
        int currentFloor = monitor.getCurrentFloor();
        int nextFloor = monitor.getNextFloor();

        monitor.toggleMoving();
        monitor.setDoorsOpen(false);
        view.closeDoors();
        view.moveLift(currentFloor, nextFloor);
        view.openDoors(nextFloor);
        monitor.setDoorsOpen(true);
        monitor.toggleMoving();
        monitor.setCurrentFloor(nextFloor);

    }
}
