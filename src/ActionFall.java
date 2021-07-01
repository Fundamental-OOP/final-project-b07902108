public class ActionFall extends BoardAction {
    private Tetromino tetromino;

    public ActionFall() {
        this.name = "F";
    }

    public ActionFall(Board board) {
        this.name = "F";
        this.board = board;
        TetrominoCell cell = board.getCurrentTetrominoCell();
        this.tetromino = cell.getTetromino();
        this.row = cell.getRow();
        this.column = cell.getColumn();
        this.relativeTetrominoCells = tetromino.getRelativeTetrominoCells();
    }

    @Override
    protected BoardAction copyAction(Board board) {
        return new ActionFall(board);
    }

    @Override
    protected boolean place() {
        int i = 1, lastValid = 0;
        boolean success;
        while (true) {
            success = this.board.placeTetromino(this.tetromino, this.row + i, this.column, true);
            if (success) {
                lastValid = i;
                i++;
            }
            else {
                break;
            }
        }
        if (lastValid > 0) {
            return this.board.placeTetromino(this.tetromino, this.row + lastValid, this.column, false);
        }
        else {
            return false;
        }
    }
}
