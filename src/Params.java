import java.util.Random;
import java.util.ResourceBundle;

/**
 * The parameters for configuration of this program.
 *
 * It is responsible for:
 * - read all the parameters configuration from a properties file, which is
 *   flexible for program as if we want to change some parameters, we don't
 *   need to re-compile the project.
 *
 * @author ngeard@unimelb.edu.au
 *         weizhuoz@student.unimelb.edu.au
 *         Weizhuo Zhang (1018329)
 */

class Params {
    /* The number of pilots */
    public final int NUM_PILOTS;

    /* The total number of tugs */
    public final int NUM_TUGS;

    /* The number of tugs used for docking */
    public final int DOCKING_TUGS;

    /* The number of tugs used for undocking */
    public final int UNDOCKING_TUGS;

    /* The time spent on the docking */
    public final int DOCKING_TIME;

    /* The time spent on the undocking */
    public final int UNDOCKING_TIME;

    /* The time spent on the unloading */
    public final int UNLOADING_TIME;

    /* The time spent on the travelling between wait zone and berth */
    public final int TRAVEL_TIME;

    /*
     * The duration time of shield from activating to deactivating.
     *
     * The usage for DEBRIS_TIME:
     *     shield activates ->
     *     operator.sleep(DEBRIS_TIME) ->
     *     shield deactivates.
     */
    public final int DEBRIS_TIME;

    /*
     * The max time between the arrival of two ships in arrival wait zone.
     *
     * The usage for MAX_ARRIVAL_INTERVAL:
     *     Ship 1 arrives ->
     *     operator.sleep(rand(0, MAX_ARRIVAL_INTERVAL)) ->
     *     Ship 2 arrives.
     */
    private final int MAX_ARRIVAL_INTERVAL;

    /*
     * The max time between the departure of two ships in departure wait zone.
     *
     * The usage for MAX_DEPARTURE_INTERVAL:
     *     Ship 1 departs ->
     *     operator.sleep(rand(0, MAX_DEPARTURE_INTERVAL)) ->
     *     Ship 2 departs.
     */
    private final int MAX_DEPARTURE_INTERVAL;

    /*
     * The max time between two space debris.
     *
     * The usage for MAX_DEBRIS_INTERVAL:
     *     shield deactivates ->
     *     operator.sleep(rand(0,MAX_DEBRIS_INTERVAL)) ->
     *     shield activates
     */
    private final int MAX_DEBRIS_INTERVAL;

    /**
     * @description
     *      The constructor for Params class. This class reads all the
     *      parameters and its values from a properties file, which makes the
     *      program more flexible. If we want to change some parameters, we
     *      don't need to re-compile the program.
     * @param configFilePath
     *      The path for properties file.
     */
    Params(String configFilePath) {
        ResourceBundle resource = ResourceBundle.getBundle(configFilePath);
        NUM_PILOTS =
            Integer.parseInt(resource.getString("NUM_PILOTS"));
        NUM_TUGS =
            Integer.parseInt(resource.getString("NUM_TUGS"));
        DOCKING_TUGS =
            Integer.parseInt(resource.getString("DOCKING_TUGS"));
        UNDOCKING_TUGS =
            Integer.parseInt(resource.getString("UNDOCKING_TUGS"));
        DOCKING_TIME =
            Integer.parseInt(resource.getString("DOCKING_TIME"));
        UNDOCKING_TIME =
            Integer.parseInt(resource.getString("UNDOCKING_TIME"));
        UNLOADING_TIME =
            Integer.parseInt(resource.getString("UNLOADING_TIME"));
        TRAVEL_TIME =
            Integer.parseInt(resource.getString("TRAVEL_TIME"));
        DEBRIS_TIME =
            Integer.parseInt(resource.getString("DEBRIS_TIME"));
        MAX_ARRIVAL_INTERVAL =
            Integer.parseInt(resource.getString("MAX_ARRIVAL_INTERVAL"));
        MAX_DEPARTURE_INTERVAL =
            Integer.parseInt(resource.getString("MAX_DEPARTURE_INTERVAL"));
        MAX_DEBRIS_INTERVAL =
            Integer.parseInt(resource.getString("MAX_DEBRIS_INTERVAL"));
    }

    /**
     * Read properties file using InputFileStream.
     */
    //private void readPropertiesFile(String configFilePath){
    //    try {
    //        InputStream inputStream =
    //                new FileInputStream(new File(configFilePath));
    //    } catch (FileNotFoundException e) {
    //        System.err.println("[ERROR]: " + configFilePath + " is not found.");
    //    }
    //}

    /**
     * Generate the random arrival interval time which is less than
     * MAX_ARRIVAL_INTERVAL
     */
    public int arrivalLapse() {
        Random rnd = new Random();
        return rnd.nextInt(this.MAX_ARRIVAL_INTERVAL);
    }

    /**
     * Generate the random departure interval time which is less than
     * MAX_DEPARTURE_INTERVAL
     */
    public int departureLapse() {
        Random rnd = new Random();
        return rnd.nextInt(this.MAX_DEPARTURE_INTERVAL);
    }

    /**
     * Generate the random interval time between two debris which is less than
     * MAX_DEBRIS_INTERVAL
     */
    public int debrisLapse() {
        Random rnd = new Random();
        return rnd.nextInt(this.MAX_DEBRIS_INTERVAL);
    }

    /**
     * Test the correctness of reading properties file.
     */
    private void testPramsFileReader() {
        System.out.println("NUM_PILOTS: " + NUM_PILOTS);
        System.out.println("NUM_TUGS: " + NUM_TUGS);
        System.out.println("DOCKING_TUGS: " + DOCKING_TUGS);
        System.out.println("UNDOCKING_TUGS: " + UNDOCKING_TUGS);
        System.out.println("DOCKING_TIME: " + DOCKING_TIME);
        System.out.println("UNDOCKING_TIME: " + UNDOCKING_TIME);
        System.out.println("UNLOADING_TIME: " + UNLOADING_TIME);
        System.out.println("TRAVEL_TIME: " + TRAVEL_TIME);
        System.out.println("DEBRIS_TIME: " + DEBRIS_TIME);
        System.out.println("MAX_ARRIVAL_INTERVAL: " + MAX_ARRIVAL_INTERVAL);
        System.out.println("MAX_DEPARTURE_INTERVAL: " + MAX_DEPARTURE_INTERVAL);
        System.out.println("MAX_DEBRIS_INTERVAL: " + MAX_DEBRIS_INTERVAL);
    }
}
