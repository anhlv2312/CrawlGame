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

import java.util.List;
import java.util.Optional;

public class CrawlView {

    private CrawlGame game;
    private BorderPane root = new BorderPane();

    private TextArea message = new TextArea();
    private TextInputDialog dialog = new TextInputDialog();


    private Button[] buttons;




    public CrawlView (CrawlGame game) {

        this.game = game;

        Canvas canvas = new Cartographer(game.getCurrentRoom());

        root.setCenter(canvas);
        root.setBottom(message);
        root.setRight(addGridPane());

        message.setEditable(false);


        dialog.initStyle(StageStyle.UNIFIED);
        dialog.setTitle(null);
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
    }

    public Scene getScene() {
        return new Scene(root);
    }



    private GridPane addGridPane() {

        buttons = new Button[10];
        for (int i = 0; i<buttons.length; i++) {
            buttons[i] = new Button();
        }
        Button btnNorth   = new Button("North");
        Button btnEast    = new Button("East");
        Button btnSouth   = new Button("South");
        Button btnWest    = new Button("West");
        Button btnLook    = new Button("Look");
        Button btnExamine = new Button("Examine");
        Button btnDrop    = new Button("Drop");
        Button btnTake    = new Button("Take");
        Button btnFight   = new Button("Fight");
        Button btnSave    = new Button("Save");


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
//            btnNorth.setDisable(true);
//            btnEast.setDisable(true);
//            btnSouth.setDisable(true);
//            btnWest.setDisable(true);
//            btnLook.setDisable(true);
//            btnExamine.setDisable(true);
//            btnDrop.setDisable(true);
//            btnTake.setDisable(true);
//            btnFight.setDisable(true);
//            btnSave.setDisable(true);
        }
    }

    private void btnSaveClick() {

    }

    public void appendLine(String line){
        if (line != null) {
            message.appendText(line + "\n");
        }
    }

    public void appendLine(List<String> lines){
        for (String line : lines) {
            if (line != null) {
                message.appendText(line+ "\n");
            }
        }
    }

}
