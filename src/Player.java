import java.util.TimerTask;
import java.util.Timer;
import java.util.List;

public class Player{
    private int index;
    private String name;
    private boolean dead = false;
    private int[] scoreList = {0, 40, 100, 300, 1200};
    private int score;
    private Board board;

    public Player(int index, String name,
                  List<BoardAction> candidateBoardActions, 
                  List<Tetromino> candidateTetrominoes) {
        this.index = index;
        this.name = name;
        this.dead = false;
        this.score = 0;
        this.board = new Board(candidateBoardActions, candidateTetrominoes);
    }

    public int getIndex() {
        return this.index;
    }

    public String getName() {
        return this.name;
    }

    public void setDead() {
        this.dead = true;
    }

    public boolean isDead() {
        return this.dead;
    }

    public int getScore() {
        return this.score;
    }

    public TetrominoCell[][] getState() {
        return this.board.getBoard();
    }

    public synchronized boolean modifyState(String action) {
        if (!this.dead) {
            return this.board.modifyBoard(action);
        }
        else {
            return false;
        }
    }

    public boolean renewState() {
        int lines = this.board.clearLines();
        this.score += scoreList[lines];
        return this.board.generateRandomTetromino();
    }

    public boolean play() {
        System.out.println("debug");
        return this.board.generateRandomTetromino();
    }
}
