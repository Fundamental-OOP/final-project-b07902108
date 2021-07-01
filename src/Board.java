import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Board {
    public final static int Rows = 20, Columns = 10;

    private TetrominoCell[][] board;
    private TetrominoCell currentTetrominoCell;
    private List<BoardAction> candidateBoardActions;
    private List<Tetromino> candidateTetrominoes;

    public Board(List<BoardAction> candidateBoardActions, 
                 List<Tetromino> candidateTetrominoes) {
        board = new TetrominoCell[Rows][Columns];
        for (int i = 0; i < Rows; i++) {
            for (int j = 0; j < Columns; j++) {
                board[i][j] = new TetrominoCell(new NullTetromino(), i, j);
            }
        }
        this.currentTetrominoCell = new TetrominoCell(new NullTetromino(), 0, 0);
        this.candidateBoardActions = candidateBoardActions;
        this.candidateTetrominoes = candidateTetrominoes;
    }

    public boolean generateRandomTetromino() {
        int randomInteger = ThreadLocalRandom.current().nextInt(0, this.candidateTetrominoes.size());
        System.out.println("randomInteger: " + String.valueOf(randomInteger));
        Tetromino nextTetromino = this.candidateTetrominoes.get(randomInteger).copyTetromino();
        int nextRow = 0;
        int nextColumn = nextTetromino.getDefaultColumn();
        if (!validColumn(nextColumn)) nextColumn = 0;
        return placeTetromino(nextTetromino, nextRow, nextColumn, false);
    }

    public TetrominoCell getCurrentTetrominoCell() {
        return this.currentTetrominoCell;
    }

    public TetrominoCell[][] getBoard() {
        return this.board;
    }

    public boolean modifyBoard(String action) {
        for (BoardAction tetrominoAction: this.candidateBoardActions) {
            if (tetrominoAction.getName().equals(action)) {
                BoardAction newAction = tetrominoAction.copyAction(this);
                return newAction.perform();
            }
        }
        throw new UnsupportedOperationException("Invalid action " + action);
    }

    public int clearLines() {
        int lineDistance = 0;
        List<TetrominoCell> nullRelativeTetrominoCells = new ArrayList<TetrominoCell>();
        for (int j = 0; j < Columns; j++) {
            nullRelativeTetrominoCells.add(new TetrominoCell(new NullTetromino(), 0, j));
        }
        for (int i = Rows - 1; i >= 0; i--) {
            boolean isLineFull = true;
            for (int j = 0; j < Columns && isLineFull; j++) {
                if (this.board[i][j].getTetromino() instanceof NullTetromino) {
                    isLineFull = false;
                }
            }
            if (isLineFull) {
                lineDistance++;
            }
            else {
                for (int j = 0; j < Columns; j++) {
                    this.board[i + lineDistance][j] = new TetrominoCell(board[i][j].getTetromino(), i + lineDistance, j);
                }
            }
            boolean success = placePattern(nullRelativeTetrominoCells, i, 0, false);
            assert success;
        }
        return lineDistance;
    }

    private boolean validRow(int row) {
        return (row >= 0 && row < Rows);
    }

    private boolean validColumn(int column) {
        return (column >= 0 && column < Columns);
    }

    public boolean placeTetromino(Tetromino tetromino, int row, int column, boolean isTry) {
        List<TetrominoCell> relativeTetrominoCells = tetromino.getRelativeTetrominoCells();
        if (placePattern(relativeTetrominoCells, row, column, isTry)) {
            if (!isTry) {
                this.currentTetrominoCell = new TetrominoCell(tetromino, row, column);
            }
            return true;
        }
        else {
            return false;
        }
    }

    public boolean placePattern(List<TetrominoCell> relativeTetrominoCells, int row, int column, boolean isTry) {
        List<TetrominoCell> absoluteTetrominoCells = new ArrayList<TetrominoCell>();
        for (TetrominoCell cell: relativeTetrominoCells) {
            Tetromino currentTetromino = cell.getTetromino();
            int absoluteRow = row + cell.getRow();
            int absoluteColumn = column + cell.getColumn();
            if (validRow(absoluteRow) && validColumn(absoluteColumn) && 
                this.board[absoluteRow][absoluteColumn].getTetromino() instanceof NullTetromino) {
                absoluteTetrominoCells.add(new TetrominoCell(currentTetromino, absoluteRow, absoluteColumn));
            }
            else {
                return false;
            }
        }
        if (!isTry) {
            for (TetrominoCell cell: absoluteTetrominoCells) {
                this.board[cell.getRow()][cell.getColumn()] = cell;
            }
        }
        return true;
    }

    public boolean removePattern(List<TetrominoCell> relativeTetrominoCells, int row, int column) {
        List<TetrominoCell> absoluteTetrominoCells = new ArrayList<TetrominoCell>();
        for (TetrominoCell cell: relativeTetrominoCells) {
            Tetromino currentTetromino = cell.getTetromino();
            int absoluteRow = row + cell.getRow();
            int absoluteColumn = column + cell.getColumn();
            if (validRow(absoluteRow) && validColumn(absoluteColumn)) { 
                absoluteTetrominoCells.add(new TetrominoCell(new NullTetromino(), absoluteRow, absoluteColumn));
            }
            else {
                return false;
            }
        }
        for (TetrominoCell cell: absoluteTetrominoCells) {
            this.board[cell.getRow()][cell.getColumn()] = cell;
        }
        return true;
    }
}
