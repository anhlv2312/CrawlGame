import java.util.LinkedList;
import java.util.List;

public class CrawlGame extends MapWalker {

    private Room root;
    private List<Room> rooms;
    private Player player;

    public CrawlGame(Player player, Room root) {
        super(root);
        this.rooms = new LinkedList<Room>();
        this.player = player;
        this.root = root;
    }

    @Override
    public void visit(Room room) {
        rooms.add(room);
    }

    public Room getRoot() {
        return root;
    }

    public String goTo(String direction) {
        return "Something prevents you from leaving\n";
    }

    public String[] look() {
        return null;
    }
    public void examine() {

    }
    public boolean drop() {
        return false;

    }
    public boolean take() {
        return false;

    }
    public void fight() {

    }
    public void save() {

    }


}
