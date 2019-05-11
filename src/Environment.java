import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.Random;

public class Environment {
    private static Environment environment =
            new Environment(Configurator.getConfigurator());
    private float vision;
    private int mapWidth;
    private int mapHeight;
    private Position[][] environmentMap;
    private ArrayList<Position> availablePosition;

    private Environment(Configurator configurator) {
        vision = configurator.getVision();
        mapWidth = configurator.MAP_WIDTH;
        mapHeight = configurator.MAP_HEIGHT;
        // Size of environmentMap is (height * width)
        // Length of rows == mapHeight
        // length of cols == mapWidth
        environmentMap = new Position[mapHeight][mapWidth];
        availablePosition = new ArrayList<Position>();
        initEnvironmentMap();
        initPositionNeighborhood();
    }

    public static Environment getEnvironment() {
        return environment;
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

    public Position acquireAvailablePosition(Person person) {
        Random random = new Random();
        int maxIndex = availablePosition.size();
        int randomIndex = random.nextInt(maxIndex);
        Position position = availablePosition.get(randomIndex);
        position.occupy(person);
        availablePosition.remove(randomIndex);
        return position;
    }

    public Position acquireMoving(Person person, Position position) {
        Position newPosition = position.move(person);
        if (null == newPosition) {
            return position;
        } else {
            availablePosition.add(position);
            newPosition.occupy(person);
            return newPosition;
        }
    }

    public Position getPosition(int coordinateX, int coordinateY) {
        return environmentMap[coordinateY][coordinateX];
    }

    private void initEnvironmentMap() {
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                // Width index is x coordinate
                // Height index is y coordinate
                environmentMap[y][x] = new Position(x, y);
                availablePosition.add(environmentMap[x][y]);
            }
        }
    }

    private void initPositionNeighborhood() {
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                environmentMap[x][y].getNeighborhood(getEnvironment());
            }
        }
    }

}
