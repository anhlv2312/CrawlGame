import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * CrawlView Class represent UI of the game including message area, map and buttons
 *
 * @author Vu Anh LE
 *
 */
public class CrawlView extends javafx.scene.Scene  {

    private CrawlGame game;
    private BorderPane root;
    private TextArea message;
    private Cartographer cartographer;
    private Map<String, Button> buttons;

    /**
     * The view of a CrawlGame
     * @param game The game object that need to be presented
     */
    public CrawlView(CrawlGame game) {
        super(new BorderPane());
        this.root = (BorderPane)this.getRoot();
        this.game = game;

        // Initial Message text area, set it non-editable
        message = new TextArea();
        message.setEditable(false);

        // Initialize The Map of button name and button objects
        buttons = new HashMap<>();

        // Initialize cartographer
        cartographer = new Cartographer(game.getRootRoom());

        // Use border Pane as the root layout, and add UI elements to pane's area
        root.setCenter(cartographer);
        root.setBottom(message);
        root.setRight(createButtons());

        appendMessage("You find yourself in " + game.getCurrentRoom().getDescription());

    }

    /**
     * Create a grid panel that contains all the control buttons
     */
    private GridPane createButtons() {
        GridPane grid = new GridPane();

        // Create new buttons and add them to the Map with their specific name
        buttons.put("North", new Button("North"));
        buttons.put("East", new Button("East"));
        buttons.put("South", new Button("South"));
        buttons.put("West", new Button("West"));
        buttons.put("Look", new Button("Look"));
        buttons.put("Examine", new Button("Examine"));
        buttons.put("Drop", new Button("Drop"));
        buttons.put("Take", new Button("Take"));
        buttons.put("Fight", new Button("Fight"));
        buttons.put("Save", new Button("Save"));

        // Init event handler
        DrawHandler handler = new DrawHandler();

        // Assign the event handler to all the button in Map
        for (Button button : buttons.values()) {
            button.setOnAction(handler);
        }

        // Add all the buttons to grid pane
        grid.add(buttons.get("North"), 1, 0);
        grid.add(buttons.get("East"), 2, 1);
        grid.add(buttons.get("South"), 1, 2);
        grid.add(buttons.get("West"), 0, 1);
        grid.add(buttons.get("Look"), 0, 3);
        grid.add(buttons.get("Examine"), 1, 3, 2, 1);
        grid.add(buttons.get("Drop"), 0, 4);
        grid.add(buttons.get("Take"), 1, 4);
        grid.add(buttons.get("Fight"), 0, 5);
        grid.add(buttons.get("Save"), 0, 6);

        return grid;
    }

    /**
     * Append the given string to the message area
     */
    private void appendMessage(String line) {
        if (line != null) {
            message.appendText(line + "\n");
        }
    }

    /**
     * Append the given List of String to the message area
     */
    private void appendMessage(List<String> lines) {
        for (String line : lines) {
            appendMessage(line);
        }
    }

    /**
     * Create a diaglog box and open it, return the value of the entered text
     */
    private String showDialog(String title) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.initStyle(StageStyle.UNIFIED);
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        Optional<String> result = dialog.showAndWait();
        return result.isPresent() ? result.get() : null;
    }

    /**
     * Refresh the UI, redraw the map and disable the button if the game is over
     */
    private void updateView() {
        // Redraw the map
        cartographer.update();
        // Disable button if the game is over
        if (game.isOver()) {
            for (Button button : buttons.values()) {
                button.setDisable(true);
            }
        }
    }

    /**
     * Private class DrawHandler that take care of all the button action
     */
    private class DrawHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent event) {
            String dialogResult;
            Button pressedButton = (Button) event.getSource();

            if (pressedButton == buttons.get("North")) {
                appendMessage(game.goTo("North"));

            } else if (pressedButton == buttons.get("East")) {
                appendMessage(game.goTo("East"));

            } else if (pressedButton == buttons.get("South")) {
                appendMessage(game.goTo("South"));

            } else if (pressedButton == buttons.get("West")) {
                appendMessage(game.goTo("West"));

            } else if (pressedButton == buttons.get("Look")) {
                appendMessage(game.look());

            } else if (pressedButton == buttons.get("Examine")) {
                dialogResult = showDialog("Examine what?");
                appendMessage(game.examine(dialogResult));

            } else if (pressedButton == buttons.get("Drop")) {
                dialogResult = showDialog("Item to drops?");
                appendMessage(game.drop(dialogResult));

            } else if (pressedButton == buttons.get("Take")) {
                dialogResult = showDialog("Take what?");
                game.take(dialogResult);

            } else if (pressedButton == buttons.get("Fight")) {
                dialogResult = showDialog("Fight what?");
                appendMessage(game.fight(dialogResult));

            } else if (pressedButton == buttons.get("Save")) {
                dialogResult = showDialog("Save filename?");
                appendMessage(game.save(dialogResult));
            }

            updateView();
        }
    }
}

