import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.canvas.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;
import java.util.Optional;

public class CrawlGui extends javafx.application.Application {

    private CrawlGame game;
    private TextArea message = new TextArea();;
    TextInputDialog dialog = new TextInputDialog();

    private Button btnNorth   = new Button("North");
    private Button btnEast    = new Button("East");
    private Button btnSouth   = new Button("South");
    private Button btnWest    = new Button("West");
    private Button btnLook    = new Button("Look");
    private Button btnExamine = new Button("Examine");
    private Button btnDrop    = new Button("Drop");
    private Button btnTake    = new Button("Take");
    private Button btnFight   = new Button("Fight");
    private Button btnSave    = new Button("Save");

    private GridPane addGridPane() {
        GridPane grid = new GridPane();

        btnNorth.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                btnExitClick("North");
            }
        });

        btnEast.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                btnExitClick("East");
            }
        });

        btnSouth.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                btnExitClick("South");
            }
        });

        btnWest.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                btnExitClick("West");
            }
        });

        btnLook.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                btnLookClick();
            }
        });

        btnExamine.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                btnExamineClick();
            }
        });

        btnDrop.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                btnDropClick();
            }
        });

        btnTake.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                btnTakeClick();
            }
        });

        btnFight.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                btnFightClick();
            }
        });

        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                btnSaveClick();
            }
        });

        grid.add(btnNorth, 1, 0);
        grid.add(btnEast, 2, 1);
        grid.add(btnSouth, 1, 2);
        grid.add(btnWest, 0, 1);
        grid.add(btnLook, 0, 3);
        grid.add(btnExamine, 1, 3, 2, 1);
        grid.add(btnDrop, 0, 4);
        grid.add(btnTake, 1, 4);
        grid.add(btnFight, 0, 5);
        grid.add(btnSave, 0, 6);

        return grid;
    }

    private void btnExitClick(String direction) {
        appendLine(game.goTo(direction));
    }

    private void btnLookClick() {
        appendLine(game.look());
    }

    private void btnExamineClick() {
        dialog.setTitle("Examine what?");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            appendLine(game.examine(result.get()));
        }
    }

    private void btnDropClick() {
        dialog.setTitle("Item to drops?");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            appendLine(game.drop(result.get()));
        }
    }

    private void btnTakeClick() {
        dialog.setTitle("Take what?");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            appendLine(game.take(result.get()));
        }
    }

    private void btnFightClick() {
        dialog.setTitle("Fight what?");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            appendLine(game.fight(result.get()));
        }
        if (game.isOver()) {
            btnNorth.setDisable(true);
            btnEast.setDisable(true);
            btnSouth.setDisable(true);
            btnWest.setDisable(true);
            btnLook.setDisable(true);
            btnExamine.setDisable(true);
            btnDrop.setDisable(true);
            btnTake.setDisable(true);
            btnFight.setDisable(true);
            btnSave.setDisable(true);
        }
    }

    private void btnSaveClick() {

    }

    private void loadGame() {
        List<String> args = this.getParameters().getRaw();

        if (args.size() != 1) {
            System.err.println("Usage: java CrawlGui mapname");
            System.exit(1);
        }

        Object[] map = MapIO.loadMap(args.get(0));

        if (map != null) {
            this.game = new CrawlGame((Player) map[0], (Room) map[1]);
        } else {
            System.err.println("Unable to load file");
            System.exit(2);
        }
    }

    private void appendLine(String line){
        if (line != null) {
            message.appendText(line + "\n");
        }
    }

    private void appendLine(List<String> lines){
        for (String line : lines) {
            if (line != null) {
                message.appendText(line+ "\n");
            }
        }
    }

    public void start(Stage stage) {
        this.loadGame();

        stage.setTitle("Crawl - Explore");
        Canvas canvas = new Cartographer(this.game.getCurrentRoom());

        message.setEditable(false);
        message.appendText("You find yourself in " + this.game.getCurrentRoom().getDescription() + "\n");

        dialog.initStyle(StageStyle.UNIFIED);
        dialog.setTitle(null);
        dialog.setHeaderText(null);
        dialog.setGraphic(null);


        BorderPane border = new BorderPane();
        border.setCenter(canvas);
        border.setBottom(message);
        border.setRight(addGridPane());

        Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
