/**
 * A cargo ship, with a unique id, that arrives at
 * the space station to deliver its cargo.
 *
 * @author ngeard@unimelb.edu.au
 *         weizhuoz@student.unimelb.edu.au
 *         Weizhuo Zhang (1018329)
 */

public class Ship {

    // a unique identifier for this cargo ship
    private int id;

    // the next ID to be allocated
    private static int nextId = 1;

    // create a new vessel with a given identifier
    Ship(int id) {
        this.id = id;
    }

    // get a new Ship instance with a unique identifier
    public static Ship getNewShip() {
        return new Ship(nextId++);
    }

    @Override
    // produce an identifying string for the cargo ship
    public String toString() {
        return "ship [" + this.id + "]";
    }
}
