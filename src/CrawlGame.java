import java.util.ArrayList;
import java.util.List;

// CrawlGame Class stores all the game information and handles player actions
public class CrawlGame {

    private Player player;
    private Room rootRoom, currentRoom;
    private boolean gameOver;

    public CrawlGame(Player player, Room root) {

        // Load the player and root room of the game
        this.player = player;
        this.currentRoom = this.rootRoom = root;

        // Initial the game over status
        gameOver = false;

        // Add the player to the root room
        currentRoom.enter(player);
    }

    // Get current room function
    public Room getCurrentRoom() {
        return currentRoom;
    }

    // Get root room function
    public Room getRootRoom() {
        return rootRoom;
    }

    // Get the game status
    public boolean isOver () {
        return gameOver;
    }


    // Handle change room action
    // Params: exitName: the name of the exit (North/East/South/West)
    // Return: status message
    public String goTo(String exitName) {
        // if the direction does not exist
        if (!currentRoom.getExits().keySet().contains(exitName)) {
            return "No door that way";
        }

        // Check if user can leave the room
        if (currentRoom.leave(player)) {
            Room nextRoom = currentRoom.getExits().get(exitName);

            // Put the Player into the next room
            nextRoom.enter(player);
            // Change current room
            currentRoom = nextRoom;
            return "You enter " + nextRoom.getDescription() + "";

        } else {
            return "Something prevents you from leaving";
        }
    }


    // Handle the look action
    // Return: the list of strings including message and item list
    public List<String> look() {
        List<String> lines = new ArrayList<>();

        // Get the room description
        lines.add(currentRoom.getDescription() + " - you see:");

        // Get all the thing in current room
        for (Thing thing : currentRoom.getContents()) {
            lines.add(" " + thing.getShortDescription());
        }

        lines.add("You are carrying:");
        double total = 0;

        // Get all the thing in player's inventory
        for (Thing thing : player.getContents()) {
            if (thing instanceof Lootable) {
                // Add the value of lootable thing to total
                total += ((Lootable) thing).getValue();
            }
            lines.add(" " + thing.getShortDescription());
        }
        lines.add("worth: " + String.format("%.1f", total) + " in total");
        return lines;
    }

    // Handle examine action
    // Params: name: name of thing
    // Return: status message
    public String examine(String name) {

        // Check the items in player inventory first
        for (Thing thing : player.getContents()) {
            if (thing.getShortDescription().equals(name)) {
                return thing.getDescription();
            }
        }

        // Then check the items in the room
        for (Thing thing : currentRoom.getContents()) {
            if (thing.getShortDescription().equals(name)) {
                return thing.getDescription();
            }
        }

        // If nothing found then return a message
        return "Nothing found with that name";
    }

    // Handle drop action
    // Params: name: name of thing
    // Return: status message
    public String drop(String name) {

        // Get the item from player inventory
        Thing thing = player.drop(name);

        // If the item is found, add it to the current room
        if (thing != null) {
            currentRoom.enter(thing);
            return null;
        }
        return "Nothing found with that name";
    }

    // Handle take action
    // Params: name: name of thing
    public void take(String name) {
        Thing item = null;
        for (Thing thing : currentRoom.getContents()) {
            // For every item, check if it has the same name, not a player and not a live critter
            if (thing.getShortDescription().equals(name)
                    && !(thing instanceof Player)
                    && !((thing instanceof Mob) && ((Mob) thing).isAlive())) {
                item = thing;
            }
        }

        // Check if item is found and the item can leave the room then add it to the player's inventory
        if (item != null && currentRoom.leave(item)) {
            player.add(item);
        }
    }

    // Handle the fight action
    // Params: description: the short description of the critter
    public String fight(String description) {
        String message = null;

        for (Thing thing : currentRoom.getContents()) {

            // Check if each thing in current room is a alive critter and have the exact description
            if (thing instanceof Critter
                    && thing.getShortDescription().equals(description)
                    && ((Critter) thing).isAlive()) {
                player.fight((Critter) thing);
                gameOver = !player.isAlive();
                message = gameOver ? "Game over" : "You won";
                break;
            }
        }
        return message;
    }

    // Handle the save game action
    public String save(String filename) {
        if (MapIO.saveMap(rootRoom, filename)) {
            return "Saved";
        } else {
            return "Unable to save";
        }
    }


}
