import javafx.scene.canvas.GraphicsContext;
import java.util.Map;
import static java.lang.Math.abs;

public class Cartographer extends javafx.scene.canvas.Canvas {

    private Room root;
    private GraphicsContext gc;
    private BoundsMapper bm;
    private int row, column, width, height, size;

    public Cartographer(Room root) {
        this.root = root;
        bm = new BoundsMapper(root);
        bm.walk();
        size = 40;
        column = abs(bm.xMax - bm.xMin) + 1;
        row = abs(bm.yMax - bm.yMin) + 1;
        width = column * size + 2;
        height = row * size + 2;
        this.setWidth(width);
        this.setHeight(height);
        gc = this.getGraphicsContext2D();
        drawMap();
    }

    public void drawMap() {
        gc.clearRect(0, 0, width, height);
        for (Map.Entry<Room, Pair> entry : bm.coords.entrySet()) {
            drawRoom(entry.getKey(), entry.getValue());
        }
    }

    private void drawRoom(Room room, Pair coord) {
        int x = (coord.x - bm.xMin) * size + 1;
        int y = (coord.y - bm.yMin) * size + 1;
        gc.strokeRect(x, y, size, size);

        for (String exit : room.getExits().keySet()) {
            drawExit(x, y, exit);
        }

        for (Thing thing : room.getContents()) {
            if (thing instanceof Player) {
                gc.fillText("@", x+2, y + 12);
                break;
            }
        }
        for (Thing thing : room.getContents()) {
            if (thing instanceof Treasure) {
                gc.fillText("$", x + (size/2) + 2, y + 12);
                break;
            }
        }
        for (Thing thing : room.getContents()) {
            if (thing instanceof Critter) {
                if (((Critter) thing).isAlive()){
                    gc.fillText("M", x + 2, y + (size/2) + 12);
                } else {
                    gc.fillText("m", x + (size/2) + 2, y + (size/2) + 12);
                }
            }
        }
    }

    private void drawExit(int x, int y, String direction){
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
