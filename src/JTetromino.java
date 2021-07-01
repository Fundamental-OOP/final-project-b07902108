import java.util.ArrayList;

public class JTetromino extends Tetromino {
    public JTetromino() {
        this.name = "J";
        this.color = ANSI_BLUE;
        this.defaultColumn = 5;
        this.relativeTetrominoCells = new ArrayList<TetrominoCell>();
        this.relativeTetrominoCells.add(new TetrominoCell(this, 0, -1));
        this.relativeTetrominoCells.add(new TetrominoCell(this, 0, 0));
        this.relativeTetrominoCells.add(new TetrominoCell(this, 0, 1));
        this.relativeTetrominoCells.add(new TetrominoCell(this, 1, 1));
    }

    @Override
    public Tetromino copyTetromino() {
        return new JTetromino();
    }
}
