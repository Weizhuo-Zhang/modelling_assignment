import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/*
    Usage: Each position indicates each grid in the map.
        Provides some manipulation methods for Position class.
    Author:
        Heming Li       804996      hemingl1@student.unimelb.edu.au
        An Luo          657605      aluo1@student.unimelb.edu.au
        Weizhuo Zhang   1018329     weizhuoz@student.unimelb.edu.au
*/

public class Position {
    private int coordinateX;
    private int coordinateY;
    private Set<Position> neighborhood;
    private Person occupied;

    public Position(int coordinateX, int coordinateY) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    // Init and get the neighborhood of this position
    public Set<Position> getNeighborhood(Environment environment) {
        if (null == neighborhood) {
            neighborhood = new HashSet<>();

            int left =
                    coordinateX - (int)Math.floor(environment.getVision());
            int right =
                    coordinateX + (int)Math.floor(environment.getVision());
            int top =
                    coordinateY - (int)Math.floor(environment.getVision());
            int bottom =
                    coordinateY + (int)Math.floor(environment.getVision());

//            // For ensure neighborhood is within the map
//            if (left < 0) {
//                left = 0;
//            }
//            if (right >= environment.getMapWidth()) {
//                right = environment.getMapWidth() - 1;
//            }
//            if (top < 0) {
//                top = 0;
//            }
//            if (bottom >= environment.getMapHeight()) {
//                bottom = environment.getMapHeight() - 1;
//            }

            // The distance between neighborhood position and this position
            // must less than or equal as vision
            for (int x = left; x <= right; x++) {
                int shiftX = x;
                // if this (x,y) is out of the map, we need to mapping this
                // point to the point within the map
                // Left and Right border are connected
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
                    } else if (y >= environment.getMapHeight()){
                        shiftY = y - environment.getMapHeight();
                        // If this point is far away
                        if (shiftY >= environment.getMapHeight()) {
                            continue;
                        }
                    }
                    // Compute the euclidean distance
                    float distance =
                            computeDistance(coordinateX, coordinateY, x, y);
                    if (distance <= environment.getVision()){
                        neighborhood.add(
                                environment.getPosition(shiftX, shiftY));
                    }
                }
            }
        }
        return neighborhood;
    }

    // Randomly pick a new position in neighborhood list for person
    public Position move(Person person) throws Exception {
        ArrayList<Position> availableNeighborhood = new ArrayList<>();
        for (Position neighbor : neighborhood) {
            if (!neighbor.isOccupied()) {
                availableNeighborhood.add(neighbor);
            }
        }

        if (0 == availableNeighborhood.size()) {
            return null;
        } else {
//            if (person.equals(this.occupied)) {
            occupied = null;
            Random random = new Random();
            int maxIndex = availableNeighborhood.size();
            int randomIndex = random.nextInt(maxIndex);
            return availableNeighborhood.get(randomIndex);
//            } else {
//                throw  new Exception("Invalid move! The parameter person is " +
//                        "not same as the person in this position");
//            }
        }
    }

    // Return the neighborhood has been occupied
    public ArrayList<Position> getOccupiedNeighborhood() {
        ArrayList<Position> occupiedNeighborhood = new ArrayList<>();
        for (Position neighbor : neighborhood) {
            if (neighbor.isOccupied()) {
                occupiedNeighborhood.add(neighbor);
            }
        }
        return occupiedNeighborhood;
    }

    // Indicate person to occupy this position
    public void occupy(Person person) throws Exception{
        if (null == person) {
            occupied = null;
            return;
        } else if (!isOccupied()) {
            occupied = person;
        } else {
            throw new Exception("Invalid occupy! This position is already " +
                    "occupied.");
        }
    }

    public Person getOccupiedPerson() {
        return occupied;
    }

    private boolean isOccupied() {
        if (null == occupied) {
            return false;
        } else {
            return true;
        }
    }

    // Compute the distance between two points
    private float computeDistance(int x1, int y1, int x2, int y2) {
        return (float)Math.sqrt(
                Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2),2));
    }
}
