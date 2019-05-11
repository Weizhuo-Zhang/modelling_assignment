public class Person {
    private Position position;

    public Person(Environment environment) {
        position = environment.acquireAvailablePosition(this);
    }

    void move(Environment environment) {
        position = environment.acquireMoving(this, position);
    }
}
