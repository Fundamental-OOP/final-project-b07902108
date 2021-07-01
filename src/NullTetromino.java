import java.util.ArrayList;

public class NullTetromino extends Tetromino {
    public NullTetromino() {
        this.name = ".";
        this.defaultColumn = 0;
        this.color = ANSI_RESET;
        this.relativeTetrominoCells = new ArrayList<TetrominoCell>();
    }

    @Override
    public Tetromino copyTetromino() {
        return new NullTetromino();
    }
}
