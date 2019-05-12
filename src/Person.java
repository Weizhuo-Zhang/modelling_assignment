public abstract class Person {
    private Position position;
    private final int maxJailTerm;

    public Person(Position position, int maxJailTerm) throws Exception {
        //position = environment.acquireAvailablePosition(this);
        this.position = position;
        this.maxJailTerm = maxJailTerm;
        this.position.occupy(this);
    }

    public Position getPosition() {
        return position;
    }

    public int getMaxJailTerm() {
        return maxJailTerm;
    }

    public void move(Position position) {
        //position = environment.acquireMoving(this, position);
        this.position = position;
    }

    abstract public void action();
}
