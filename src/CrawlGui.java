import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.canvas.*;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class CrawlGui extends javafx.application.Application {

    private CrawlGame game;

    private Stage stage;
    private Canvas canvas;
    private Button btnNorth, btnEast, btnSouth, btnWest, btnLook, btnExamine, btnDrop, btnTake, btnFight, btnSave;

    private TextArea textArea;


    public GridPane addGridPane() {
        GridPane grid = new GridPane();

        btnNorth   = new Button("North");
        btnEast    = new Button("East");
        btnSouth   = new Button("South");
        btnWest    = new Button("West");
        btnLook    = new Button("Look");
        btnExamine = new Button("Examine");
        btnDrop    = new Button("Drop");
        btnTake    = new Button("Take");
        btnFight   = new Button("Fight");
        btnSave    = new Button("Save");

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
        grid.add(btnSouth, 0, 1);
        grid.add(btnWest, 1, 2);
        grid.add(btnLook, 0, 3);
        grid.add(btnExamine, 1, 3, 2, 1);
        grid.add(btnDrop, 0, 4);
        grid.add(btnTake, 1, 4);
        grid.add(btnFight, 0, 5);
        grid.add(btnSave, 0, 6);

        return grid;
    }

    public void btnExitClick(String direction) {
        String message = this.game.goTo(direction);
        this.textArea.appendText(message);
    }

    public void btnLookClick() {

    }

    public void btnExamineClick() {

    }

    public void btnDropClick() {

    }

    public void btnTakeClick() {

    }

    public void btnFightClick() {

    }

    public void btnSaveClick() {

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

    public void start(Stage stage) {
        this.loadGame();

        this.stage = stage;
        this.stage.setTitle("Crawl - Explore");

        this.canvas = new Cartographer(this.game.getRoot());

        this.textArea = new TextArea();
        this.textArea.setEditable(false);
        this.textArea.appendText("You find yourself in " + this.game.getRoot().getDescription() + "\n");

        BorderPane border = new BorderPane();
        border.setCenter(this.canvas);
        border.setBottom(this.textArea);
        border.setRight(addGridPane());

        Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
