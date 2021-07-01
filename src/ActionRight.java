public class ActionRight extends BoardAction {
    private Tetromino tetromino;

    public ActionRight() {
        this.name = "R";
    }

    public ActionRight(Board board) {
        this.name = "R";
        this.board = board;
        TetrominoCell cell = board.getCurrentTetrominoCell();
        this.tetromino = cell.getTetromino();
        this.row = cell.getRow();
        this.column = cell.getColumn();
        this.relativeTetrominoCells = tetromino.getRelativeTetrominoCells();
    }

    @Override
    protected BoardAction copyAction(Board board) {
        return new ActionRight(board);
    }

    @Override
    protected boolean place() {
        return this.board.placeTetromino(this.tetromino, this.row, this.column + 1, false);
    }
}
