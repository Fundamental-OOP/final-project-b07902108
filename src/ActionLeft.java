public class ActionLeft extends BoardAction {
    private Tetromino tetromino;

    public ActionLeft() {
        this.name = "L";
    }

    public ActionLeft(Board board) {
        this.name = "L";
        this.board = board;
        TetrominoCell cell = board.getCurrentTetrominoCell();
        this.tetromino = cell.getTetromino();
        this.row = cell.getRow();
        this.column = cell.getColumn();
        this.relativeTetrominoCells = tetromino.getRelativeTetrominoCells();
    }

    @Override
    protected BoardAction copyAction(Board board) {
        return new ActionLeft(board);
    }

    @Override
    protected boolean place() {
        return this.board.placeTetromino(this.tetromino, this.row, this.column - 1, false);
    }
}
