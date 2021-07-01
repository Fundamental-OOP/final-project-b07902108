import java.util.List;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;
import java.lang.Thread;
import java.io.PrintWriter;

public class TetrisGame extends Thread {
    public static final int timeoutSecond = 1;

    private int playerNum;
    private List<ClientThread> clientThreads;
    private List<Player> players;
    private List<Timer> timers;
    private int AliveCount;
    private Player winner, lastAlivePlayer;
    private int highScore;
    private boolean end;
    private TetrisServer server;

    public TetrisGame(List<ClientThread> clientThreads,
                      List<BoardAction> candidateBoardActions, 
                      List<Tetromino> candidateTetrominoes,
                      TetrisServer server) {
        this.playerNum = clientThreads.size();
        this.clientThreads = clientThreads;
        this.players = new ArrayList<Player>();
        this.timers = new ArrayList<Timer>();
        for (int i = 0; i < playerNum; i++) {
            this.players.add(new Player(i, clientThreads.get(i).getName_(),
                                        candidateBoardActions,
                                        candidateTetrominoes));
            this.timers.add(new Timer());
        }
        this.AliveCount = playerNum;
        this.winner = this.lastAlivePlayer = null;
        this.highScore = 0;
        this.end = false;
        this.server = server;
    }
    
    public Player getWinner() {
        return this.winner;
    }

    public synchronized boolean getOrSetEnd(boolean get) {
        if (get) {
            return this.end;
        }
        else {
            this.end = true;
            return this.end;
        }
    }

    public synchronized void timeoutEvent(Player player) {
        System.out.println("[debug] " + player.getName());
        if (!player.modifyState("D")) {
            if (!player.renewState()) {
                player.setDead();
            }
        }
        if (player.isDead()) {
            this.AliveCount--;
        }
        boolean callGameFinal = false;
        if (this.AliveCount == 1 && this.players.size() != 1) {
            if (this.lastAlivePlayer == null) {
                for (Player p: this.players) {
                    if (!p.isDead()) {
                        this.lastAlivePlayer = p;
                    }
                    else {
                        int currentScore = p.getScore();
                        if (currentScore > this.highScore) {
                            this.highScore = currentScore;
                            this.winner = p;
                        }
                    }
                }
                this.winner = lastAlivePlayer;
            }
            int currentScore = this.lastAlivePlayer.getScore();
            if (currentScore > this.highScore) {
                this.highScore = currentScore;
                this.winner = lastAlivePlayer;
                callGameFinal = true;
            }
        }
        else if (this.AliveCount == 0) {
            callGameFinal = true;
        }
        if (callGameFinal) {
            if (this.players.size() == 1) {
                this.winner = this.players.get(0); 
            }
            gameFinal();
        }
        else {
            int i = player.getIndex();
            PlayerTimeoutEvent timeoutEvent = new PlayerTimeoutEvent(this, this.players.get(i));
            timers.get(i).schedule(timeoutEvent, timeoutSecond * 1000);
            displayPlayerBoard(player);
        }
    }

    @Override
    public void run() {
        System.out.println("[debug] start new game");
        for (int i = 0; i < this.playerNum; i++) {
            PlayerTimeoutEvent timeoutEvent = new PlayerTimeoutEvent(this, this.players.get(i));
            timers.get(i).schedule(timeoutEvent, timeoutSecond * 1000);
            boolean success = this.players.get(i).play();
            assert success;
        }
        while (!getOrSetEnd(true));
    }

    public void displayPlayerBoard(Player player) {
        List<ScoreEntry> scoreBoard = new ArrayList<ScoreEntry>();
        List<Player> otherPlayers = new ArrayList<Player>();
        for (Player p: this.players) {
            scoreBoard.add(new ScoreEntry(p.getName(), p.getScore()));
            if (p != player) {
                otherPlayers.add(p);
            }
        }
        GUI playerInfo = new GUI(player, otherPlayers, scoreBoard);
        String buffer = playerInfo.getDisplayBuffer();
        for (int i = 0; i < this.players.size(); i++) {
            if (player == this.players.get(i)) {
                this.clientThreads.get(i).sendMsg(buffer);
                return;            
            }
        }
        throw new UnsupportedOperationException("Invalid clientThread");
    }

    public void gameFinal() {
        for (ClientThread t: this.clientThreads) {
            t.sendMsg(winner.getName() + " wins.\n\r");        
            t.atexit();
        }
        System.exit(0);
    }

    public synchronized void playerAction(ClientThread clientThread, String action) {
        for (int i = 0; i < this.clientThreads.size(); i++) {
            if (this.clientThreads.get(i) == clientThread) {
                Player player = this.players.get(i);
                boolean success = player.modifyState(action);
                if (success) {
                    displayPlayerBoard(player);
                }
            }
        }
    }
}
