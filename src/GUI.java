import java.util.List;
import java.util.ArrayList;

public class GUI {
    public static final int Rows = 42, Columns = 100;
    public static final int maxSubBoard = 4;
    public static final String ANSI_RESET = "\033[0m";
    public static final String texture = "X";
    public static final String margin = "#";
    public static final String blank = " ";
    public static final String dashBoardTag = ">  Dash Board  <";

    private Player player;
    private List<Player> otherPlayers;
    private List<ScoreEntry> scoreBoard;
    private List<String> lines;

    public GUI(Player player, List<Player> otherPlayers, List<ScoreEntry> scoreBoard) {
        this.player = player;
        this.otherPlayers = otherPlayers;
        this.scoreBoard = scoreBoard;
        this.lines = new ArrayList<String>();
    }

    public String getDisplayBuffer() {
        TetrominoCell[][] playerCells = this.player.getState();
        String tripleTexture = texture + texture + texture;
        String tripleMargin = margin + margin + margin;
        String tripleBlank = blank + blank + blank;
        for (int i = 0; i < Board.Rows; i++) {
            String line = "";
            line += tripleMargin;
            for (int j = 0; j < Board.Columns; j++) {
                Tetromino tetromino = playerCells[i][j].getTetromino();
                if (tetromino instanceof NullTetromino) {
                    line += tripleBlank;
                }
                else {
                    line += (tetromino.getColor() + tripleTexture);
                }
            }
            line += (ANSI_RESET + tripleMargin + tripleBlank);
            this.lines.add(line);
            this.lines.add(line);
        }

        String currentLine = tripleMargin + tripleMargin + blank;
        String currentLine2 = tripleMargin + tripleMargin + blank;
        String name = this.player.getName();
        currentLine += (name + blank);
        for (int i = 0; i < name.length() + 1; i++) {
            currentLine2 += blank;
        }
        for (int i = 0; i < 10 - name.length(); i++) {
            currentLine += margin;
            currentLine2 += margin;
        }
        for (int i = 0; i < Board.Columns - 4; i++) {
            currentLine += tripleMargin;
            currentLine2 += tripleMargin;
        }
        currentLine += tripleBlank;
        currentLine2 += tripleBlank;
        this.lines.add(currentLine);
        this.lines.add(currentLine2);

        addSubBoard(0, 0);
        addSubBoard(1, 0);
        addSubBoard(2, 21);
        addSubBoard(3, 21);

        addDashBoard();

        String buffer = "";
        int lineCount = this.lines.size();
        for (int i = 0; i < lineCount - 1; i++) {
            String line = this.lines.get(i);
            buffer += (line + "\n\r");
        }
        buffer += this.lines.get(lineCount - 1) + "\n\r";
        return buffer;
    }

    private void addSubBoard(int playerIndex, int lineIndex) {
        String blank15 = "";
        for (int i = 0; i < 15; i++) {
            blank15 += blank;
        }
        String tripleBlank = blank + blank + blank;
        if (playerIndex < this.otherPlayers.size()) {
            Player player = this.otherPlayers.get(playerIndex);
            TetrominoCell[][] cells = player.getState(); 
            for (int i = 0; i < Board.Rows; i++) {
                String line = this.lines.get(lineIndex + i);
                line += margin;
                for (int j = 0; j < Board.Columns; j++) {
                    Tetromino tetromino = cells[i][j].getTetromino();
                    if (tetromino instanceof NullTetromino) {
                        line += blank;
                    }
                    else {
                        line += (tetromino.getColor() + texture);
                    }
                }
                line += (ANSI_RESET + margin + tripleBlank);
                this.lines.set(lineIndex + i, line);
            }
            String line = this.lines.get(lineIndex + Board.Rows);
            String name = player.getName();
            line += (margin + blank + name + blank);
            for (int i = 0; i < Board.Columns - name.length() - 1; i++) {
                line += margin;
            }
            line += tripleBlank;
            this.lines.set(lineIndex + Board.Rows, line);
        }
        else {
            for (int i = 0; i < Board.Rows + 1; i++) {
                String line = this.lines.get(lineIndex + i);
                line += blank15;
                this.lines.set(lineIndex + i, line);
            }
        }
    }

    private void addDashBoard() {
        String line = this.lines.get(0);
        line += dashBoardTag;
        this.lines.set(0, line);
        for (int i = 0; i < this.scoreBoard.size(); i++) {
            ScoreEntry entry = this.scoreBoard.get(i);
            line = this.lines.get(i + 2);
            line += (entry.getName() + " " + entry.getScore());
            this.lines.set(i + 2, line);
        }
    }
}
