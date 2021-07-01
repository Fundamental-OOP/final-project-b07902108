import java.util.ArrayList;

public class ITetromino extends Tetromino {
    public ITetromino() {
        this.name = "I";
        this.color = ANSI_CYAN;
        this.defaultColumn = 5;
        this.relativeTetrominoCells = new ArrayList<TetrominoCell>();
        this.relativeTetrominoCells.add(new TetrominoCell(this, 0, -2));
        this.relativeTetrominoCells.add(new TetrominoCell(this, 0, -1));
        this.relativeTetrominoCells.add(new TetrominoCell(this, 0, 0));
        this.relativeTetrominoCells.add(new TetrominoCell(this, 0, 1));
    }

    @Override
    public Tetromino copyTetromino() {
        return new ITetromino();
    }
}
