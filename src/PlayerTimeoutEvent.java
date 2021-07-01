import java.util.TimerTask;

public class PlayerTimeoutEvent extends TimerTask {
    TetrisGame game;
    Player player;

    public PlayerTimeoutEvent(TetrisGame game, Player player) {
        this.game = game;
        this.player = player;
    }

    @Override
    public void run() {
        game.timeoutEvent(player);
    }
}
