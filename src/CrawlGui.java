import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;

public class CrawlGui extends javafx.application.Application {

    public void start(Stage stage) {

        List<String> args = this.getParameters().getRaw();
        if (args.size() != 1) {
            System.err.println("Usage: java CrawlGui mapname");
            System.exit(1);
        }

        CrawlGame game = new CrawlGame();

        if (!game.loadGame(args.get(0))) {
            System.err.println("Unable to load file");
            System.exit(2);
        }
        stage.setTitle("Crawl - Explore");

        CrawlView view = new CrawlView(game);
        view. appendLine("You find yourself in " + game.getCurrentRoom().getDescription());
        stage.setScene(view.getScene());
        stage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }

}
