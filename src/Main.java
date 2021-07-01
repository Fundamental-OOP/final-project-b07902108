import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

public class Main {
    public static int maxPlayerNum = 2;
    public static List<BoardAction> candidateBoardActions = new ArrayList<BoardAction>();
    static {
        candidateBoardActions.add(new ActionLeft());
        candidateBoardActions.add(new ActionRight());
        candidateBoardActions.add(new ActionDown());
        candidateBoardActions.add(new ActionFall());
        candidateBoardActions.add(new ActionLeft());
        candidateBoardActions.add(new ActionRotateCounterClockwise());
        candidateBoardActions.add(new ActionRotateClockwise());
    }
    public static List<Tetromino> candidateTetrominoes = new ArrayList<Tetromino>();
    static {    // IOTJLSZ
        candidateTetrominoes.add(new ITetromino());
        candidateTetrominoes.add(new OTetromino());
        candidateTetrominoes.add(new JTetromino());
        candidateTetrominoes.add(new TTetromino());
        candidateTetrominoes.add(new LTetromino());
        candidateTetrominoes.add(new STetromino());
        candidateTetrominoes.add(new ZTetromino());
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("usage: java Main [port]");
            System.exit(1);
        }
        TetrisServer tetrisServer = 
            new TetrisServer(Integer.valueOf(args[0]), 
                             maxPlayerNum,
                             candidateBoardActions, 
                             candidateTetrominoes);
        tetrisServer.run();
    }
}
