/**
 * Consumes unloaded cargo ships from the departure zone.
 *
 * @author ngeard@unimelb.edu.au
 *
 */

public class Berth {
    private String name;

    public Berth(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
