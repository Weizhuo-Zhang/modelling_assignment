import java.util.ArrayList;
import java.util.Random;

public class Position {
    private int coordinateX;
    private int coordinateY;
    private ArrayList<Position> neighborhood;
    private Person occupied;

    public Position(int coordinateX, int coordinateY) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public ArrayList<Position> getNeighborhood(Environment environment) {
        if (null == neighborhood) {
            neighborhood = new ArrayList<Position>();

            int left =
                    coordinateX - (int)Math.floor(environment.getVision());
            int right =
                    coordinateX + (int)Math.floor(environment.getVision());
            int top =
                    coordinateY - (int)Math.floor(environment.getVision());
            int bottom =
                    coordinateY + (int)Math.floor(environment.getVision());
            if (left < 0) {
                left = 0;
            }
            if (right >= environment.getMapWidth()) {
                right = environment.getMapWidth() - 1;
            }
            if (top < 0) {
                top = 0;
            }
            if (bottom >= environment.getMapHeight()) {
                bottom = environment.getMapHeight() - 1;
            }
            for (int x = left; x < right; x++) {
                for (int y = top; y < bottom; y++) {
                    float distance =
                            computeDistance(x, coordinateX, y, coordinateY);
                    if (distance <= environment.getVision()){
                        neighborhood.add(environment.getPosition(x, y));
                    }
                }
            }
        }
        return neighborhood;
    }

    public Position move(Person person) {
        ArrayList<Position> availableNeighborhood = new ArrayList<Position>();
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
            int maxIndex = neighborhood.size();
            int randomIndex = random.nextInt(maxIndex);
            return availableNeighborhood.get(randomIndex);
        }
    }

    public void occupy(Person person) {
        occupied = person;
    }

    public boolean isOccupied(){
        if (null == occupied) {
            return false;
        } else {
            return true;
        }
    }

    private float computeDistance(int x1, int y1, int x2, int y2) {
        return (float)Math.sqrt(
                Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2),2));
    }
}
