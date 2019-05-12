import java.util.ArrayList;
import java.util.Random;

/*
    Usage: Allocate and protect the environment which contains a grid map.
        The map is consisted of multiple instances of Position.
    Author:
        Heming Li       804996      hemingl1@student.unimelb.edu.au
        An Luo          657605      aluo1@student.unimelb.edu.au
        Weizhuo Zhang   1018329     weizhuoz@student.unimelb.edu.au
 */

public class Environment {
    private float vision;
    private static int mapWidth;
    private static int mapHeight;
    private static float governmentLegitimacy;
    private static int maxJailTerm;
    private static boolean movementSwitch;
    // The grid map of this environment
    private static Position[][] environmentMap;
    // The available position of the environment
    private ArrayList<Position> availablePosition;

    // Use singleton pattern to ensure there is only one Environment instance
    private static Environment environment;

    private Environment(
            float vision,
            int mapWidth,
            int mapHeight,
            int maxJailTerm,
            float governmentLegitimacy,
            boolean movementSwitch) {
        this.vision = vision;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.maxJailTerm = maxJailTerm;
        this.governmentLegitimacy = governmentLegitimacy;
        this.movementSwitch = movementSwitch;
        // Size of environmentMap is (height * width)
        // Length of rows == mapHeight
        // length of cols == mapWidth
        environmentMap = new Position[mapHeight][mapWidth];
        availablePosition = new ArrayList<>();
        initEnvironmentMap();
    }

    // Getter method of this singleton class
    public static Environment getEnvironment(
            float vision,
            int mapWidth,
            int mapHeight,
            int maxJailTerm,
            float governmentLegitimacy,
            boolean movementSwitch) {
        if (null == environment) {
            environment = new Environment(
                    vision,
                    mapWidth,
                    mapHeight,
                    maxJailTerm,
                    governmentLegitimacy,
                    movementSwitch);
            initPositionNeighborhood(environment);
            return environment;
        } else {
            return environment;
        }
    }

    public float getVision() {
        return vision;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public static int getMaxJailTerm() {
        return maxJailTerm;
    }

    public static float getGovernmentLegitimacy() {
        return governmentLegitimacy;
    }

    public static boolean isMovement() {
        return movementSwitch;
    }

    public Position getPosition(int x, int y) {
        return environmentMap[y][x];
    }

    // Acquire available position in this map randomly
    public Position acquireAvailablePosition(Person person) throws Exception {
        int maxIndex = availablePosition.size();
        // If there is no any position available, this function will throws a
        // Exception
        if (0 != maxIndex) {
            Random random = new Random();
            int randomIndex = random.nextInt(maxIndex);
            Position position = availablePosition.get(randomIndex);
            position.occupy(person);
            availablePosition.remove(randomIndex);
            return position;
        } else {
            throw new Exception("No position available!");
        }
    }

    // Acquire moving to a new position within vision
    public Position acquireMoving(Person person) throws Exception{
        Position newPosition = person.getPosition().move(person);
        // If there is no any new position to move, the person will stay
        // the same position.
        if (null == newPosition) {
            return person.getPosition();
        } else {
            availablePosition.add(person.getPosition());
            availablePosition.remove(newPosition);
            newPosition.occupy(person);
            return newPosition;
        }
    }

    public void releasePosition(Person person) throws Exception {
        Position position = person.getPosition();
        availablePosition.add(position);
        position.occupy(null);
        person.setPosition(null);
    }

    // Init the environment map
    private void initEnvironmentMap() {
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                // Width index is x coordinate
                // Height index is y coordinate
                environmentMap[y][x] = new Position(x, y);
                availablePosition.add(environmentMap[y][x]);
            }
        }
    }

    // Init the neighborhood position of each position in this map
    private static void initPositionNeighborhood(Environment environment) {
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                // Width index is x coordinate
                // Height index is y coordinate
                environmentMap[y][x].getNeighborhood(environment);
            }
        }
    }

    private void testEnvironmentMap() {
        for (int x = 0; x < mapWidth; x++) {
            System.out.print(x + "\t");
            for (int y = 0; y < mapHeight; y++) {
                if (null == environmentMap[y][x]) {
                    System.out.println("Null position exists");
                } else {
                    System.out.print(y + " ");
                }
            }
            System.out.println("");
        }
    }

    private void testAvailablePosition() {
        System.out.println(availablePosition.size());
    }

    private static void testPositionNeighborhood() {
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                // Width index is x coordinate
                // Height index is y coordinate
                ArrayList<Position> neighbor =
                        environmentMap[y][x].getNeighborhood(environment);
                System.out.print(neighbor.size() + "\t");
            }
            System.out.println("");
        }
    }

    public void testAcquirePosition() {
        System.out.println(this.availablePosition.size());
    }
}
