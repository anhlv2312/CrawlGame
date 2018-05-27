import javafx.stage.Stage;
import java.util.List;

/**
 * CrawlGame Graphic User Interface
 *
 * @author Vu Anh LE
 *
 */
public class CrawlGui extends javafx.application.Application {

    /**
     * Launch the App
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start the App and show the stage
     */
    public void start(Stage stage) {

        // Get the arguments from command line
        List<String> args = this.getParameters().getRaw();

        // If the number of argument is other than 1 then exit with a message
        if (args.size() != 1) {
            System.err.println("Usage: java CrawlGui mapname");
            System.exit(1);
        }

        // Load the game from file
        CrawlGame game = loadGame(args.get(0));
        if (game == null) {
            System.err.println("Unable to load file");
            System.exit(2);
        }

        // Initialize the UI
        CrawlView view = new CrawlView(game);
        stage.setScene(view);

        // Set the title of the application
        stage.setTitle("Crawl - Explore");
        stage.show();
    }

    /** Load game from a file an return the game data
     * @param filename The name of the file to load data from
     * @return the {@link CrawlGame CrawGame} object
     */
    private CrawlGame loadGame(String filename) {

        Object[] map = MapIO.loadMap(filename);

        // if return CrawlGame if the map is loaded successfully, otherwise return null
        return map != null ? new CrawlGame((Player) map[0], (Room) map[1]) : null;
    }

}
