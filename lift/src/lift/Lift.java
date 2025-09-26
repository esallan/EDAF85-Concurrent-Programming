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

    private synchronized void updatePassengers() {
        unloadPassengers();
        loadPassengers();
    }

    private synchronized void loadPassengers() {
        while (!monitor.liftFull() && monitor.passengerWantToEnter()) {
            try {
                wait(WAITING_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void unloadPassengers() {
        while (!monitor.isEmpty() && monitor.passengerWantToExit()) {
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

        // Stäng dörrar innan hiss rör sig
        view.closeDoors();

        // Flytta hiss grafiskt
        view.moveLift(currentFloor, nextFloor);

        // Uppdatera hissens nuvarande våning
        monitor.setCurrentFloor(nextFloor);

        // Kolla om dörrarna ska öppnas (någon vill på/av)
        monitor.updateDoors();
        if (monitor.isDoorsOpen()) {
            view.openDoors(nextFloor);
        }
    }
}
