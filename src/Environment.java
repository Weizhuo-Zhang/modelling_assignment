import java.util.ArrayList;
import java.util.Random;

/**
 * Usage: Allocate and protect the environment which contains a grid map. The map is consisted of
 * multiple instances of Position.
 *
 * @author Weizhuo Zhang (1018329) - weizhuoz@student.unimelb.edu.au
 * @author Heming Li (804996) - hemingl1@student.unimelb.edu.au
 * @author An Luo (657605) - aluo1@student.unimelb.edu.au
 */
public class Environment {
  private float vision;
  private static int mapWidth;
  private static int mapHeight;
  private static float governmentLegitimacy;
  private static int maxJailTerm;
  private static boolean movementSwitch;
  /** The grid map of this environment. */
  private static Position[][] environmentMap;
  /** The available position of the environment. */
  private ArrayList<Position> availablePosition;
  /** Use singleton pattern to ensure there is only one Environment instance. */
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

  /**
   * Getter method of this singleton class
   *
   * @param vision Vision of people.
   * @param mapWidth Width of the map.
   * @param mapHeight Height of the map.
   * @param maxJailTerm Max jail term.
   * @param governmentLegitimacy Legitimacy of government.
   * @param movementSwitch Flag indicating if movement is true or false.
   * @return Current environment instance.
   */
  static Environment getEnvironment(
      float vision,
      int mapWidth,
      int mapHeight,
      int maxJailTerm,
      float governmentLegitimacy,
      boolean movementSwitch) {
    if (null == environment) {
      environment =
          new Environment(
              vision, mapWidth, mapHeight, maxJailTerm, governmentLegitimacy, movementSwitch);
      initPositionNeighborhood(environment);
      return environment;
    } else {
      return environment;
    }
  }

  /**
   * Get the vision of people.
   *
   * @return Vision of people.
   */
  float getVision() {
    return vision;
  }

  /**
   * Get the width of map.
   *
   * @return Width of map.
   */
  int getMapWidth() {
    return mapWidth;
  }

  /**
   * Get the height of map.
   *
   * @return Height of map.
   */
  int getMapHeight() {
    return mapHeight;
  }

  /**
   * Get the max jail term.
   *
   * @return Max jail term.
   */
  static int getMaxJailTerm() {
    return maxJailTerm;
  }

  /**
   * Get government legitimacy.
   *
   * @return Government legitimacy.
   */
  static float getGovernmentLegitimacy() {
    return governmentLegitimacy;
  }

  /**
   * Get the movement flag.
   *
   * @return Movement flag indicating if movement is set to true or false.
   */
  static boolean isMovement() {
    return movementSwitch;
  }

  /**
   * Get the position specified in provided coordinate.
   *
   * @param x x coordinate of the position being looked for.
   * @param y y coordinate of the position being looked for.
   * @return The position of provided coordinate.
   */
  Position getPosition(int x, int y) {
    return environmentMap[y][x];
  }

  /**
   * Acquire available position in this map randomly
   *
   * @param person Person who is going to the provided map.
   * @return Position person specified is going to move to.
   * @throws Exception
   */
  Position acquireAvailablePosition(Person person) throws Exception {
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

  /**
   * Move the specified person to a new position if there is one, and release the current position
   * he/she holds.
   *
   * @param person Person who is going to move.
   * @return Position of the person after movement..
   * @throws Exception
   */
  Position moveThePerson(Person person) throws Exception {
    Position newPosition = person.getPosition().move();
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

  /**
   * Release position of current person, as the person is arrested by the cop Cop will occupy the
   * position of this person, and the position of the cop will be set as available
   *
   * @param agentArrested Agent who is going to release the position as he/she is arrested.
   * @param cop Cop who arrested the agent.
   * @throws Exception
   */
  void releasePosition(Person agentArrested, Person cop) throws Exception {
    Position position = agentArrested.getPosition();
    Position copPosition = cop.getPosition();
    cop.setPosition(position);
    position.occupy(null);
    position.occupy(cop);
    availablePosition.add(copPosition);
    copPosition.occupy(null);
  }

  /** Init the environment map */
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

  /**
   * Init the neighborhood position of each position in this map
   *
   * @param environment
   */
  private static void initPositionNeighborhood(Environment environment) {
    for (int x = 0; x < mapWidth; x++) {
      for (int y = 0; y < mapHeight; y++) {
        // Width index is x coordinate
        // Height index is y coordinate
        environmentMap[y][x].getNeighborhood(environment);
      }
    }
  }
}
