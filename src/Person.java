/*
    Usage: Superclass for cop and agent
    Author:
        Heming Li       804996      hemingl1@student.unimelb.edu.au
        An Luo          657605      aluo1@student.unimelb.edu.au
        Weizhuo Zhang   1018329     weizhuoz@student.unimelb.edu.au
*/

public abstract class Person {
    // Two different person type
    public static final String AGENT = "Agent";
    public static final String COP = "Cop";

    private Position position;
    private Environment environment;

    public Person(Environment environment) throws Exception {
        this.environment = environment;
        this.position = environment.acquireAvailablePosition(this);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Environment getPersonEnvironment() {
        return environment;
    }

    // Move to another position
    public void move() throws Exception {
        position = environment.acquireMoving(this);
    }

    // Abstract action() method for subclass
    abstract public void action() throws Exception;
}
