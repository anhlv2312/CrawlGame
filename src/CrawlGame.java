import java.util.ArrayList;
import java.util.List;

public class CrawlGame {

    private Player player;
    private Room rootRoom, currentRoom;
    private boolean gameOver;

    public CrawlGame(Player player, Room root) {
        this.player = player;
        this.currentRoom = root;
        gameOver = false;
        currentRoom.enter(player);
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public Room getRootRoom() {
        return rootRoom;
    }

    public boolean isOver () {
        return gameOver;
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
                    System.out.println("Cant take player()");
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
        String message = null;
        for (Thing thing : currentRoom.getContents()) {
            if (thing instanceof Critter && ((Critter) thing).isAlive()) {
                player.fight((Critter) thing);
                gameOver = !player.isAlive();
                message = gameOver ? "Game over" : "You won";
                break;
            }
        }
        return message;
    }

    public String save(String filename) {
        if (MapIO.saveMap(rootRoom, filename)) {
            return "Saved";
        } else {
            return "Unable to save";
        }
    }


}
