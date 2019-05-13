public abstract class Person {
    public static final String AGENT = "Agent";
    public static final String COP = "COP";

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

    public void move() throws Exception {
        position = environment.acquireMoving(this);
    }

    abstract public void action() throws Exception;
}
