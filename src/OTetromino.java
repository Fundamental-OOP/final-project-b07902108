import java.util.ArrayList;

public class OTetromino extends Tetromino {
    public OTetromino() {
        this.name = "O";
        this.color = ANSI_YELLOW;
        this.defaultColumn = 4;
        this.relativeTetrominoCells = new ArrayList<TetrominoCell>();
        this.relativeTetrominoCells.add(new TetrominoCell(this, 0, 0));
        this.relativeTetrominoCells.add(new TetrominoCell(this, 0, 1));
        this.relativeTetrominoCells.add(new TetrominoCell(this, 1, 0));
        this.relativeTetrominoCells.add(new TetrominoCell(this, 1, 1));
    }

    @Override
    public Tetromino copyTetromino() {
        return new OTetromino();
    }
}
