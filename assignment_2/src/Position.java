import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Usage: Each position indicates each grid in the map. Provides some manipulation methods for
 * Position class.
 *
 * @author Weizhuo Zhang (1018329) - weizhuoz@student.unimelb.edu.au
 * @author Heming Li (804996) - hemingl1@student.unimelb.edu.au
 * @author An Luo (657605) - aluo1@student.unimelb.edu.au
 */
class Position {
  private int coordinateX;
  private int coordinateY;
  private Set<Position> neighborhood;
  private Person occupied;

  Position(int coordinateX, int coordinateY) {
    this.coordinateX = coordinateX;
    this.coordinateY = coordinateY;
    this.neighborhood = null;
  }

  /**
   * Get the neighborhood of this position
   *
   * @param environment Environment this position belongs to.
   * @return A set of position that are the neighborhoods of current position.
   */
  Set<Position> getNeighborhood(Environment environment) {
    if (null == neighborhood) {
      neighborhood = new HashSet<>();

      int left = coordinateX - (int) Math.floor(environment.getVision());
      int right = coordinateX + (int) Math.floor(environment.getVision());
      int top = coordinateY - (int) Math.floor(environment.getVision());
      int bottom = coordinateY + (int) Math.floor(environment.getVision());

      // The distance between neighborhood position and this position must less than or equal as
      // vision.
      for (int x = left; x <= right; x++) {
        int shiftX = x;
        // if this (x,y) is out of the map, we need to mapping this point to the point within the
        // map Left and Right border are connected.
        if (x < 0) {
          shiftX = environment.getMapWidth() + x;
          // If this point is far away
          if (shiftX < 0) {
            continue;
          }
        } else if (x >= environment.getMapWidth()) {
          shiftX = x - environment.getMapWidth();
          // If this point is far away
          if (shiftX >= environment.getMapWidth()) {
            continue;
          }
        }

        for (int y = top; y <= bottom; y++) {
          int shiftY = y;
          // Top and Bottom border are connected
          if (y < 0) {
            shiftY = environment.getMapHeight() + y;
            // If this point is far away
            if (shiftY < 0) {
              continue;
            }
          } else if (y >= environment.getMapHeight()) {
            shiftY = y - environment.getMapHeight();
            // If this point is far away
            if (shiftY >= environment.getMapHeight()) {
              continue;
            }
          }

          // Compute the euclidean distance
          float distance = computeDistance(coordinateX, coordinateY, x, y);
          if (distance <= environment.getVision()) {
            neighborhood.add(environment.getPosition(shiftX, shiftY));
          }
        }
      }
    }
    return neighborhood;
  }

  /**
   * Randomly pick a new position in neighborhood list for person
   *
   * @return Return a position which is the neighbor of current position and is available. Otherwise
   *     null.
   * @throws Exception
   */
  Position move() throws Exception {
    ArrayList<Position> availableNeighborhood = new ArrayList<>();
    for (Position neighbor : neighborhood) {
      if (!neighbor.isOccupied()) {
        availableNeighborhood.add(neighbor);
      }
    }

    if (0 == availableNeighborhood.size()) {
      return null;
    } else {
      occupied = null;
      Random random = new Random();
      int maxIndex = availableNeighborhood.size();
      int randomIndex = random.nextInt(maxIndex);
      return availableNeighborhood.get(randomIndex);
    }
  }

  /**
   * Get the occupied neighborhood list.
   *
   * @return Occupied neighbor positions list.
   */
  ArrayList<Position> getOccupiedNeighborhood() {
    ArrayList<Position> occupiedNeighborhood = new ArrayList<>();
    for (Position neighbor : neighborhood) {
      if (neighbor.isOccupied()) {
        occupiedNeighborhood.add(neighbor);
      }
    }
    return occupiedNeighborhood;
  }

  /**
   * Assign the provided person to current position/
   *
   * @param person Person who is going to occupy this position.
   * @throws Exception Exception is thrown when this position is already occupied.
   */
  void occupy(Person person) throws Exception {
    if (null == person) {
      occupied = null;
    } else if (!isOccupied()) {
      occupied = person;
    } else {
      throw new Exception("Invalid occupy! This position is already occupied.");
    }
  }

  /**
   * Get the person who occupies current position.
   *
   * @return Person who occupies current position.
   */
  Person getOccupiedPerson() {
    return occupied;
  }

  /**
   * Get the flag value indicating if current position is occupied.
   *
   * @return true if current position is occupied, otherwise false.
   */
  private boolean isOccupied() {
    return null != occupied;
  }

  /**
   * Compute the distance between two points
   *
   * @param x1 x coordinate of the first position.
   * @param y1 y coordinate of the first position.
   * @param x2 x coordinate of the second position.
   * @param y2 y coordinate of the second position.
   * @return Distance between the two position.
   */
  private float computeDistance(int x1, int y1, int x2, int y2) {
    return (float) Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2), 2));
  }
}
