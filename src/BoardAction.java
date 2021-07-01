import java.util.List;

public abstract class BoardAction {
    protected String name;
    protected Board board;
    protected int row, column;
    protected List<TetrominoCell> relativeTetrominoCells;

    public String getName() {
        return this.name;
    }

    public boolean perform() {
        boolean success = remove();
        assert success;
        if (place()) {
            return true;
        }
        else {
            success = rollback();
            assert success;
            return false;
        }
    }

    protected boolean remove() {
        return this.board.removePattern(this.relativeTetrominoCells, this.row, this.column);
    }

    protected boolean rollback() {
        return this.board.placePattern(this.relativeTetrominoCells, this.row, this.column, false);
    }

    protected abstract BoardAction copyAction(Board board);
    protected abstract boolean place();
}
