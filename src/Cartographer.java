import javafx.scene.canvas.GraphicsContext;
import java.util.Map;
import static java.lang.Math.abs;

public class Cartographer extends javafx.scene.canvas.Canvas {

    private Room root;
    private GraphicsContext gc;
    private BoundsMapper bm;
    private int width, height, size, padding;

    public Cartographer(Room root) {
        this.root = root;
        bm = new BoundsMapper(root);
        bm.walk();
        size = 40;
        padding = 20;

        // Calculate the number of row and column of the map
        int column = abs(bm.xMax - bm.xMin) + 1;
        int row = abs(bm.yMax - bm.yMin) + 1;

        // Work out the size of the canvas, add a padding of 1/2 size to each edge
        // + 1 size for the padding
        width = (column * size) + (padding * 2);
        height = (row * size) + (padding * 2);

        // Set the height and width of the canvas
        this.setWidth(width);
        this.setHeight(height);

        // Initialize the Graphic Context
        gc = this.getGraphicsContext2D();

        drawMap();
    }

    public void drawMap() {
        // Clear the canvas
        gc.clearRect(0, 0, width, height);

        // For each room, draw it
        for (Map.Entry<Room, Pair> entry : bm.coords.entrySet()) {
            drawRoom(entry.getKey(), entry.getValue());
        }
    }

    private void drawRoom(Room room, Pair coord) {

        boolean hasPlayer, hasTreasure, hasAliveMob, hasDeadMob;
        hasPlayer = hasTreasure = hasAliveMob = hasDeadMob = false;

        // Calculate the coord of the top left corner
        int x = (coord.x - bm.xMin) * size + padding;
        int y = (coord.y - bm.yMin) * size + padding;

        // Draw a square
        gc.strokeRect(x, y, size, size);

        // Draw exits of the room
        for (String exit : room.getExits().keySet()) {
            drawExit(x, y, exit);
        }


        for (Thing thing : room.getContents()) {
            // Check if there is any player in the room
            if (thing instanceof Player) {
                hasPlayer = true;
            }
            // Check if there is any treasure in the room
            if (thing instanceof Treasure) {
                hasTreasure = true;
            }
            // Check if there is any alive mob
            if (thing instanceof Critter && ((Critter) thing).isAlive()) {
                hasAliveMob = true;
            }
            // Check if there is any dead mob
            if (thing instanceof Critter && !((Critter) thing).isAlive()) {
                hasDeadMob = true;
            }
        }


        // Draw any thing in the room
        if (hasPlayer) {
            gc.fillText("@", x+2, y + 12);
        }
        if (hasTreasure) {
            gc.fillText("$", x + (size/2) + 2, y + 12);
        }
        if (hasAliveMob) {
            gc.fillText("M", x + 2, y + (size/2) + 12);
        }
        if (hasDeadMob) {
            gc.fillText("m", x + (size/2) + 2, y + (size/2) + 12);
        }
    }

    // Draw the exit of the room
    private void drawExit(int x, int y, String direction){
        // Check the direction if it is North, East, South or West
        switch (direction) {
            case "North":
                gc.strokeLine(x + (size/2), y, x + (size/2), y + 3);
                break;
            case "East":
                gc.strokeLine(x + size, y + (size/2), x + size - 3, y + (size/2));
                break;
            case "South":
                gc.strokeLine(x + (size/2), y + size, x + (size/2), y + size - 3);
                break;
            case "West":
                gc.strokeLine(x, y + (size/2), x + 3, y + (size/2));
                break;
        }

    }

}
