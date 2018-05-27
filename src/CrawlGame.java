import java.util.ArrayList;
import java.util.List;

/**
 * CrawlGame Class stores all the game information and handles player actions
 *
 * @author Vu Anh LE
 *
 */
public class CrawlGame {

    private Player player;
    private Room rootRoom, currentRoom;
    private boolean gameOver;

    /**
     * Initialize CrawlGame
     * @param player The player of the game
     * @param root The root room of the game
     */
    public CrawlGame(Player player, Room root) {

        // Load the player and root room of the game
        this.player = player;
        this.currentRoom = this.rootRoom = root;

        // Initial the game over status
        gameOver = false;

        // Add the player to the root room
        currentRoom.enter(player);
    }

    /**
     * Get current room
     * @return The room that player is currently in
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Get root room
     * @return the root room of the map
     */
    public Room getRootRoom() {
        return rootRoom;
    }

    /**
     * Get the game status
     * @return true if the game is over, false otherwise
     */
    public boolean isOver () {
        return gameOver;
    }

    /**
     * Move to another room
     * @param exitName  the name of the exit (North/East/South/West)
     * @return the status message
     */
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

    /**
     * Look around the room
     * @return the list of strings including message and item list
     */
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

    /**
     * Examine the thing in room
     * @param name Name of the thing to examine
     * @return status message
     */
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

    /**
     * Drop the thing in the player's inventory
     * @param name Name of the thing to drop
     * @return status message
     */
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

    /**
     * Take an item in the room
     * @param name Name of the thing to take
     */
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


    /**
     * Fight action
     * @param description The short description of the critter
     */
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

    /**
     * Save action
     * @param filename The name of the file to be saved to
     */
    public String save(String filename) {
        if (MapIO.saveMap(rootRoom, filename)) {
            return "Saved";
        } else {
            return "Unable to save";
        }
    }


}
