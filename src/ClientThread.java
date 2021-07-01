import java.lang.Thread;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.DataOutputStream;

public class ClientThread extends Thread {
    public static final String welcomeMsg = "Enter your name: ";
    public static final String waitMsg = "waiting for the game starting ...\n";
    public static final int maxNameLen = 8;

    private TetrisServer server;
    private Socket socket;
    private boolean ready;
    private boolean gameStarted;
    BufferedReader in;
    PrintWriter out;
    private String name;

    public ClientThread(TetrisServer server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        this.ready = false;
        this.gameStarted = false;
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            sendMsg(welcomeMsg);
            String name = this.in.readLine();
            if (name.length() > maxNameLen || name.length() == 0) {
                atexit();
                return;
            }
            this.name = name;
            getOrSetReady(false);
            sendMsg("Hi, " + this.name + "! " + waitMsg);
            this.server.startGame(this);
            while (!getOrSetGameStart(true));
            System.out.println("wait input");
            char[] buffer = new char[1024];
            int readBytes = in.read(buffer, 0, 1024); // read dummies
            while (true) {
                buffer = new char[1024];
                readBytes = in.read(buffer, 0, 1024);
                System.out.println(readBytes + " " + new String(buffer));
                if (readBytes == 1 && buffer[0] == 'z') {
                    this.server.operate(this, "RTC");
                }
                else if (readBytes == 1 && buffer[0] == 'x') {
                    this.server.operate(this, "RT");
                }
                else if (readBytes == 3 && buffer[0] == 27 && buffer[1] == 91 && buffer[2] == 66) {
                    this.server.operate(this, "F");
                }
                else if (readBytes == 3 && buffer[0] == 27 && buffer[1] == 91 && buffer[2] == 67) {
                    this.server.operate(this, "R");
                }
                else if (readBytes == 3 && buffer[0] == 27 && buffer[1] == 91 && buffer[2] == 68) {
                    this.server.operate(this, "L");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Client error, close socket.");
        }
        atexit();
        return;
    }

    public void sendMsg(String msg) {
        try {
            this.out.print(msg); 
            this.out.flush(); 
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendBytes(byte[] bytes) {
        try {
            DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
            out.write(bytes);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean getOrSetReady(boolean get) {
        if (get) {
            return this.ready;
        }
        else {
            this.ready = true;
            return true;
        }
    }

    public synchronized boolean getOrSetGameStart(boolean get) {
        if (get) {
            return this.gameStarted;
        }
        else {
            this.gameStarted = true;
            return true;
        }
    }

    public void atexit() {
        try {
            this.server.modifyClientThread(this, null, -1);
            this.socket.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getName_() {
        return this.name;
    }
}
