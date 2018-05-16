public class CrawlController {
    private CrawlView view;
    private CrawlGame game;

    public CrawlController(CrawlGame game, CrawlView view) {
        this.view = view;
        this.game = game;
        view.appendLine("You find yourself in " + game.getCurrentRoom().getDescription());
    }
}
