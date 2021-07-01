import java.util.ArrayList;

public class ZTetromino extends Tetromino {
    public ZTetromino() {
        this.name = "Z";
        this.defaultColumn = 5;
        this.color = ANSI_RED;
        this.relativeTetrominoCells = new ArrayList<TetrominoCell>();
        this.relativeTetrominoCells.add(new TetrominoCell(this, 0, -1));
        this.relativeTetrominoCells.add(new TetrominoCell(this, 0, 0));
        this.relativeTetrominoCells.add(new TetrominoCell(this, 1, 0));
        this.relativeTetrominoCells.add(new TetrominoCell(this, 1, 1));
    }

    @Override
    public Tetromino copyTetromino() {
        return new ZTetromino();
    }
}
