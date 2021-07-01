import java.util.ArrayList;

public class TTetromino extends Tetromino {
    public TTetromino() {
        this.name = "T";
        this.color = ANSI_PURPLE;
        this.defaultColumn = 5;
        this.relativeTetrominoCells = new ArrayList<TetrominoCell>();
        this.relativeTetrominoCells.add(new TetrominoCell(this, 0, -1));
        this.relativeTetrominoCells.add(new TetrominoCell(this, 0, 0));
        this.relativeTetrominoCells.add(new TetrominoCell(this, 0, 1));
        this.relativeTetrominoCells.add(new TetrominoCell(this, 1, 0));
    }

    @Override
    public Tetromino copyTetromino() {
        return new TTetromino();
    }
}
