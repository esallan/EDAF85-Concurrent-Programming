package lift;

public class Lift extends Thread {
    LiftView view;
    Monitor monitor;
    Person[] persons;

    public Lift(LiftView liftView, Monitor monitor, Person[] persons) {
        this.view = liftView;
        this.monitor = monitor;
        this.persons = persons;
    }

    public void run() {
        // TODO Auto-generated method stub
        super.run();
    }
}
