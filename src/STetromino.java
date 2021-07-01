import java.util.ArrayList;

public class STetromino extends Tetromino {
    public STetromino() {
        this.name = "S";
        this.defaultColumn = 5;
        this.color = ANSI_GREEN; 
        this.relativeTetrominoCells = new ArrayList<TetrominoCell>();
        this.relativeTetrominoCells.add(new TetrominoCell(this, 0, 0));
        this.relativeTetrominoCells.add(new TetrominoCell(this, 0, 1));
        this.relativeTetrominoCells.add(new TetrominoCell(this, 1, -1));
        this.relativeTetrominoCells.add(new TetrominoCell(this, 1, 0));
    }

    @Override
    public Tetromino copyTetromino() {
        return new STetromino();
    }
}
