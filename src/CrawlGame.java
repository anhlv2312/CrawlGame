import java.util.LinkedList;
import java.util.List;

public class CrawlGame extends MapWalker {

    private Room rootRoom, currentRoom;
    private List<Room> rooms;
    private Player player;

    public CrawlGame(Player player, Room root) {
        super(root);
        this.rooms = new LinkedList<Room>();
        this.player = player;
        this.rootRoom = this.currentRoom = root;
        this.currentRoom.enter(player);
    }

    @Override
    public void visit(Room room) {
        rooms.add(room);
    }

    public Room getRootRoom() {
        return rootRoom;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public String goTo(String direction) {
        if (!currentRoom.getExits().keySet().contains(direction)) {
            return "No door that way\n";
        }

        if (currentRoom.leave(player)) {
            Room nextRoom = currentRoom.getExits().get(direction);
            nextRoom.enter(player);
            currentRoom = nextRoom;
            return "You enter " + nextRoom.getDescription() + "\n";
        } else {
            return "Something prevents you from leaving\n";
        }
    }

    public String look() {
        return currentRoom.getContents().toString();
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
