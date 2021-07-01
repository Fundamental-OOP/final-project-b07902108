public class TetrominoCell {
    private Tetromino tetromino;
    private int row, column;

    public TetrominoCell(Tetromino tetromino, int row, int column) {
        this.tetromino = tetromino;
        this.row = row;
        this.column = column;
    }

    public Tetromino getTetromino() {
        return this.tetromino;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }
}
