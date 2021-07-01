import java.util.List;
import java.util.ArrayList;

public class ActionRotateClockwise extends BoardAction {
    private Tetromino tetromino;

    public ActionRotateClockwise() {
        this.name = "RT";
    }

    public ActionRotateClockwise(Board board) {
        this.name = "RT";
        this.board = board;
        TetrominoCell cell = board.getCurrentTetrominoCell();
        this.tetromino = cell.getTetromino();
        this.row = cell.getRow();
        this.column = cell.getColumn();
        this.relativeTetrominoCells = tetromino.getRelativeTetrominoCells();
    }

    @Override
    protected BoardAction copyAction(Board board) {
        return new ActionRotateClockwise(board);
    }

    @Override
    protected boolean place() {
        List<TetrominoCell> rotatedRelativeTetrominoCells = new ArrayList<TetrominoCell>();
        for (TetrominoCell cell: this.relativeTetrominoCells) {
            int newRow = cell.getColumn();
            int newColumn = -cell.getRow();
            rotatedRelativeTetrominoCells.add(new TetrominoCell(this.tetromino, newRow, newColumn));
        }
        if (this.board.placePattern(rotatedRelativeTetrominoCells, this.row, this.column, false)) {
            this.tetromino.setRelativeTetrominoCells(rotatedRelativeTetrominoCells);
            return true;
        }
        return false;
    }
}
