import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;
import java.util.ResourceBundle;

class Params {
    public final int NUM_PILOTS;

    public final int NUM_TUGS;

    public final int DOCKING_TUGS;

    public final int UNDOCKING_TUGS;

    public final int DOCKING_TIME;

    public final int UNDOCKING_TIME;

    public final int UNLOADING_TIME;

    public final int TRAVEL_TIME;

    public final int DEBRIS_TIME;

    private final int MAX_ARRIVAL_INTERVAL;

    private final int MAX_DEPARTURE_INTERVAL;

    private final int MAX_DEBRIS_INTERVAL;

    public Params(String configFilePath) {
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

    public int arrivalLapse() {
        Random rnd = new Random();
        return rnd.nextInt(this.MAX_ARRIVAL_INTERVAL);
    }

    public int departureLapse() {
        Random rnd = new Random();
        return rnd.nextInt(this.MAX_DEPARTURE_INTERVAL);
    }

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
