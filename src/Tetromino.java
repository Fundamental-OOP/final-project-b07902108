import java.util.List;

public abstract class Tetromino {
    public static final String ANSI_RESET = "\033[0m";
    public static final String ANSI_CYAN = "\033[0;36m";
    public static final String ANSI_YELLOW = "\033[1;33m";
    public static final String ANSI_PURPLE = "\033[0;35m";
    public static final String ANSI_ORANGE = "\033[0;33m";
    public static final String ANSI_GREEN = "\033[1;32m";
    public static final String ANSI_BLUE = "\033[0;34m";
    public static final String ANSI_RED = "\033[0;31m";

    protected int defaultColumn;
    protected List<TetrominoCell> relativeTetrominoCells;
    protected String name;
    protected String color;

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
    }

    public List<TetrominoCell> getRelativeTetrominoCells() {
        return this.relativeTetrominoCells;
    }

    public void setRelativeTetrominoCells(List<TetrominoCell> relativeTetrominoCells) {
        this.relativeTetrominoCells = relativeTetrominoCells;
    }

    public int getDefaultColumn() {
        return this.defaultColumn;
    }

    public abstract Tetromino copyTetromino();
}
