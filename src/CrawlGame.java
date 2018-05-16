import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CrawlGame extends MapWalker {

    private Room rootRoom, currentRoom;
    private List<Room> rooms;
    private Player player;
    private boolean gameOver;

    public CrawlGame(Player player, Room root) {
        super(root);
        this.rooms = new LinkedList<Room>();
        this.player = player;
        rootRoom = this.currentRoom = root;
        currentRoom.enter(player);
        gameOver = false;

    }

    @Override
    public void visit(Room room) {
        rooms.add(room);
    }

    public boolean isOver () {
        return gameOver;
    }

    public Room getRootRoom() {
        return rootRoom;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public String goTo(String direction) {
        if (!currentRoom.getExits().keySet().contains(direction)) {
            return "No door that way";
        }

        if (currentRoom.leave(player)) {
            Room nextRoom = currentRoom.getExits().get(direction);
            nextRoom.enter(player);
            currentRoom = nextRoom;
            return "You enter " + nextRoom.getDescription() + "";
        } else {
            return "Something prevents you from leaving";
        }
    }

    public List<String> look() {
        List<String> lines = new ArrayList<>();
        lines.add(currentRoom.getDescription() + " - you see:");
        for (Thing thing : currentRoom.getContents()) {
            lines.add(" " + thing.getShortDescription());
        }
        lines.add("You are carrying:");
        double total = 0;
        for (Thing thing : player.getContents()) {
            if (thing instanceof Lootable) {
                total += ((Lootable) thing).getValue();
            }
            lines.add(" " + thing.getShortDescription());
        }
        lines.add("worth: " + String.format("%.1f", total) + " in total");
        return lines;
    }

    public String examine(String name) {
        for (Thing thing : player.getContents()) {
            if (thing.getShortDescription().equals(name)) {
                return thing.getDescription();
            }
        }
        for (Thing thing : currentRoom.getContents()) {
            if (thing.getShortDescription().equals(name)) {
                return thing.getDescription();
            }
        }
        return "Nothing found with that name";
    }
    public String drop(String name) {
        Thing thing = player.drop(name);
        if (thing != null) {
            currentRoom.enter(thing);
            return null;
        }
        return "Nothing found with that name";
    }
    public String take(String name) {
        for (Thing thing : currentRoom.getContents()) {
            if (thing.getShortDescription().equals(name)) {
                if (thing instanceof Player) {
                    System.out.println("Cant take player");
                    break;
                } else if (thing instanceof Mob && ((Mob) thing).isAlive()) {
                    System.out.println("The mob is alive");
                    break;
                } else if (!currentRoom.leave(thing)) {
                    System.out.println("Cant leave the room");
                    break;
                }
                player.add(thing);
            }
        }
        return null;
    }

    public String fight(String description) {
        for (Thing thing : currentRoom.getContents()) {
            if (thing instanceof Critter) {
                Critter critter = (Critter) thing;
                if (critter.isAlive()) {
                    player.fight(critter);
                    if (player.isAlive()) {
                        return "You won";
                    } else {
                        gameOver = true;
                        return "Game over";
                    }
                }
                break;
            }
        }
        return null;
    }

    public void save() {

    }


}
