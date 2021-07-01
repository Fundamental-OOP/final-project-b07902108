public class ActionDown extends BoardAction {
    private Tetromino tetromino;

    public ActionDown() {
        this.name = "D";
    }

    public ActionDown(Board board) {
        this.name = "D";
        this.board = board;
        TetrominoCell cell = board.getCurrentTetrominoCell();
        this.tetromino = cell.getTetromino();
        this.row = cell.getRow();
        this.column = cell.getColumn();
        this.relativeTetrominoCells = tetromino.getRelativeTetrominoCells();
    }

    @Override
    protected BoardAction copyAction(Board board) {
        return new ActionDown(board);
    }

    @Override
    protected boolean place() {
        return this.board.placeTetromino(this.tetromino, this.row + 1, this.column, false);
    }
}
