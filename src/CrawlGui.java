import javafx.stage.Stage;
import java.util.List;

public class CrawlGui extends javafx.application.Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        List<String> args = this.getParameters().getRaw();
        if (args.size() != 1) {
            System.err.println("Usage: java CrawlGui mapname");
            System.exit(1);
        }

        CrawlGame game = loadGame(args.get(0));

        if (game == null) {
            System.err.println("Unable to load file");
            System.exit(2);
        }

        CrawlView view = new CrawlView(game);

        stage.setTitle("Crawl - Explore");
        stage.setScene(view.getScene());
        stage.show();
    }

    private CrawlGame loadGame(String filename) {
        Object[] map = MapIO.loadMap(filename);
        if (map != null) {
            Player player = (Player) map[0];
            Room root = (Room) map[1];
            return new CrawlGame(player, root);
        }
        return null;
    }

}
