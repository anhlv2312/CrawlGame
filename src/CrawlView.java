import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
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

public class CrawlView {

    private CrawlGame game;
    private BorderPane root;
    private TextArea message;
    private TextInputDialog dialog;
    private Map<String, Button> buttons;

    public CrawlView(CrawlGame game) {

        this.game = game;

        root = new BorderPane();
        message = new TextArea();
        message.setEditable(false);

        dialog = new TextInputDialog();
        dialog.initStyle(StageStyle.UNIFIED);
        dialog.setHeaderText(null);
        dialog.setGraphic(null);

        buttons = new HashMap<>();
        Canvas canvas = new Cartographer(game.getCurrentRoom());


        root.setCenter(canvas);
        root.setBottom(message);
        root.setRight(createButtons());

        appendLine("You find yourself in " + game.getCurrentRoom().getDescription());
    }

    public Scene getScene() {
        return new Scene(root);
    }

    private GridPane createButtons() {
        GridPane grid = new GridPane();

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

        DrawHandler handler = new DrawHandler();
        for (Button button : buttons.values()) {
            button.setOnAction(handler);
        }

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

    private void appendLine(String line) {
        if (line != null) {
            message.appendText(line + "\n");
        }
    }

    private void appendLine(List<String> lines) {
        for (String line : lines) {
            if (line != null) {
                message.appendText(line + "\n");
            }
        }
    }

    private String showDialog(String title) {
        dialog.getEditor().clear();
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            return(result.get());
        }

        return null;

    }


    private class DrawHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent event) {
            Button pressedButton = (Button) event.getSource();
            String message = null;
            List<String> messages = null;
            if (pressedButton == buttons.get("North")) {
                message = game.goTo("North");
            } else if (pressedButton == buttons.get("East")) {
                message = game.goTo("East");
            } else if (pressedButton == buttons.get("South")) {
                message = game.goTo("South");
            } else if (pressedButton == buttons.get("West")) {
                message = game.goTo("West");
            } else if (pressedButton == buttons.get("Look")) {
                messages = game.look();
                appendLine(messages);
            } else if (pressedButton == buttons.get("Examine")) {
                showDialog("Examine what?");
            } else if (pressedButton == buttons.get("Drop")) {
                String result = showDialog("Item to drops?");
                message = game.drop(result);
            } else if (pressedButton == buttons.get("Take")) {
                String result = showDialog("Take what?");
                message = game.take(result);
            } else if (pressedButton == buttons.get("Fight")) {
                String result = showDialog("Fight what?");
                message = game.fight(result);
                if (game.isOver()) {
                    for (Button button : buttons.values()) {
                        button.setDisable(true);
                    }
                }
            } else if (pressedButton == buttons.get("Save")) {
                showDialog("Save filename?");
            }
            appendLine(message);
        }
    }
}

