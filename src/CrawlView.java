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
        dialog = new TextInputDialog();
        buttons = new HashMap<>();

        Canvas canvas = new Cartographer(game.getCurrentRoom());

        root.setCenter(canvas);
        root.setBottom(message);
        root.setRight(addGridPane());

        addButtonHandler(new DrawHandler());

        message.setEditable(false);


        dialog.initStyle(StageStyle.UNIFIED);
        dialog.setTitle(null);
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
    }

    public Scene getScene() {
        return new Scene(root);
    }


    private void addButtonHandler(EventHandler<ActionEvent> handler) {
        for (Button button : buttons.values()) {
            button.setOnAction(handler);
        }
    }


    private GridPane addGridPane() {
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

        GridPane grid = new GridPane();

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

    public void appendLine(String line) {
        if (line != null) {
            message.appendText(line + "\n");
        }
    }

    public void appendLine(List<String> lines) {
        for (String line : lines) {
            if (line != null) {
                message.appendText(line + "\n");
            }
        }
    }

    public void showDialog() {
    }


    private class DrawHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent event) {
            Button pressedButton = (Button) event.getSource();

            if (pressedButton == buttons.get("North")) {
                appendLine(game.goTo("North"));
            } else if (pressedButton == buttons.get("East")) {
                appendLine(game.goTo("East"));
            } else if (pressedButton == buttons.get("South")) {
                appendLine(game.goTo("South"));
            } else if (pressedButton == buttons.get("West")) {
                appendLine(game.goTo("West"));
            } else if (pressedButton == buttons.get("Look")) {
                appendLine(game.look());
            } else if (pressedButton == buttons.get("Examine")) {
                dialog.setTitle("Examine what?");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    appendLine(game.examine(result.get()));
                }
            } else if (pressedButton == buttons.get("Drop")) {
                dialog.setTitle("Item to drops?");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    appendLine(game.drop(result.get()));
                }
            } else if (pressedButton == buttons.get("Take")) {
                dialog.setTitle("Take what?");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    appendLine(game.take(result.get()));
                }
            } else if (pressedButton == buttons.get("Fight")) {
                dialog.setTitle("Fight what?");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    appendLine(game.fight(result.get()));
                }
                if (game.isOver()) {
                    for (Button button : buttons.values()) {
                        button.setDisable(true);
                    }
                }
            } else if (pressedButton == buttons.get("Save")) {
                showDialog();
            }

        }
    }
}

