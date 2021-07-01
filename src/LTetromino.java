import java.util.ArrayList;

public class LTetromino extends Tetromino {
    public LTetromino() {
        this.name = "L";
        this.color = ANSI_ORANGE;
        this.defaultColumn = 5;
        this.relativeTetrominoCells = new ArrayList<TetrominoCell>();
        this.relativeTetrominoCells.add(new TetrominoCell(this, 0, -1));
        this.relativeTetrominoCells.add(new TetrominoCell(this, 0, 0));
        this.relativeTetrominoCells.add(new TetrominoCell(this, 0, 1));
        this.relativeTetrominoCells.add(new TetrominoCell(this, 1, -1));
    }

    @Override
    public Tetromino copyTetromino() {
        return new LTetromino();
    }
}
