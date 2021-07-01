import java.util.ArrayList;
import java.util.List;
import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.IOException;

public class TetrisServer {
    public static final long countDownStartTime = 3;
    public static final long[] countDownTime = {30, 20, 10, 5, 3, 2, 1};
    public static final String sorryMsg = "Sorry, the game has already started, or there are too many players!\n";
    public static final byte[] LINEMODE = {(byte)255, (byte)253, 34, (byte)255, (byte)251, 1};

    private int port;
    private int maxPlayerNum;
    private TetrisGame game;
    private List<BoardAction> candidateBoardActions;
    private List<Tetromino> candidateTetrominoes;
    private ServerSocket serverSocket;
    private List<ClientThread> clientThreads;
    private boolean inGame;
    private int currentPlayerNum, readyPlayerNum;

    public TetrisServer(int port, int maxPlayerNum,
                        List<BoardAction> candidateBoardActions, 
                        List<Tetromino> candidateTetrominoes) {
        this.port = port;
        this.maxPlayerNum = maxPlayerNum;
        this.candidateBoardActions = candidateBoardActions;
        this.candidateTetrominoes = candidateTetrominoes;
        this.inGame = false;
        this.currentPlayerNum = 0;
        this.readyPlayerNum = 0;
        this.clientThreads = new ArrayList<ClientThread>();
    }

    public void run() throws IOException {
        try {
            this.serverSocket = new ServerSocket(this.port);
        } 
        catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (true) {
            System.out.println("debug");
            Socket newSocket = null;
            try {
                newSocket = serverSocket.accept();
            }
            catch (IOException e) {
                System.err.println("I/O error: " + e);
                System.exit(1);
            }

            if (newSocket != null && (addOrGetCurrentPlayerNum(false, 0) >= this.maxPlayerNum || setOrGetInGame(false, false))) {
                PrintWriter out = new PrintWriter(newSocket.getOutputStream(), true);
                out.println(sorryMsg);
                newSocket.close();
                continue;
            }

            addOrGetCurrentPlayerNum(true, 1);
            ClientThread newThread = new ClientThread(this, newSocket);
            modifyClientThread(newThread, null, 1);
            newThread.start();
        }
    }

    public synchronized List<ClientThread> modifyClientThread(ClientThread clientThread, String msg, int option) {
        // option: 1 -> add, 0 -> send msg, -1 -> delete, 3 -> set game start
        if (option == 1) {
            if (setOrGetInGame(false, false)) {
                clientThread.sendMsg(sorryMsg);
                clientThread.atexit();
                return null;
            }
            this.clientThreads.add(clientThread);
            return null;
        }
        else if (option == -1) {
            for (int i = 0; i < this.clientThreads.size(); i++) {
                if (this.clientThreads.get(i) == clientThread) {
                    addOrGetCurrentPlayerNum(true, -1);
                    if (clientThread.getOrSetReady(true)) {
                        addOrGetReadyPlayerNum(true, -1);
                    }
                    this.clientThreads.remove(i);
                    break;
                }
            }
            return null;
        }
        else if (option == 0) {
            for (ClientThread t: this.clientThreads) {
                boolean ready = t.getOrSetReady(true);
                if (ready) {
                    t.sendMsg(msg);
                }
            }
            return null;
        }
        else if (option == 3) {
            setOrGetInGame(true, true);
            List<ClientThread> readyClientThreads = new ArrayList<ClientThread>();
            for (int i = 0; i < this.clientThreads.size(); i++) {
                ClientThread t = this.clientThreads.get(i);
                boolean ready = t.getOrSetReady(true);
                if (ready) {
                    t.getOrSetGameStart(false);
                    System.out.println("server set game start");
                    readyClientThreads.add(t);
                }
            }
            return readyClientThreads;
        }
        throw new UnsupportedOperationException("Invalid option " + option);
    }

    public synchronized int addOrGetCurrentPlayerNum(boolean add, int amount) {
        if (add) {
            this.currentPlayerNum += amount;
            return 0;
        }
        else {
            return this.currentPlayerNum;
        }
    }

    public synchronized int addOrGetReadyPlayerNum(boolean add, int amount) {
        if (add) {
            this.readyPlayerNum += amount;
            return 0;
        }
        else {
            return this.readyPlayerNum;
        }
    }

    private synchronized boolean setOrGetInGame(boolean set, boolean state) {
        if (set) {
            this.inGame = state;
            return true;
        }
        else {
            return this.inGame;
        }
    }

    public void startGame(ClientThread clientThread) {
        if (setOrGetInGame(false, false)) {
            clientThread.sendMsg(sorryMsg);
            clientThread.atexit();
            return;
        }
        addOrGetReadyPlayerNum(true, 1);
        if (addOrGetReadyPlayerNum(false, 0) == 1) {
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < countDownTime.length; i++) {
                while (true) {
                    long endTime = System.currentTimeMillis();
                    long elapsedTime = (endTime - startTime) / 1000;
                    if (elapsedTime >= (countDownStartTime - countDownTime[i])) {
                        String msg = "The game will start in " + String.valueOf(countDownTime[i]) + " seconds ...\n";
                        modifyClientThread(null, msg, 0);
                        break;
                    }
                }
            }
            List<ClientThread> readyClientThreads = modifyClientThread(null, null, 3);
            for (ClientThread t: readyClientThreads) {
                t.sendBytes(LINEMODE);
            }
            this.game = new TetrisGame(
                    readyClientThreads,
                    this.candidateBoardActions, 
                    this.candidateTetrominoes,
                    this);
            this.game.start();
        }
        return;
    }

    public synchronized void operate(ClientThread clientThread, String action) {
        this.game.playerAction(clientThread, action);
    }
}
