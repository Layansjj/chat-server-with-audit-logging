
package com.mycompany.client.server;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer {

    private static final int PORT = 5000;
    private static final String LOG_FILE = "server_log.txt";
    private static final String AUDIT_LOG = "audit.log";
    private static final AuditLogger audit = new AuditLogger(AUDIT_LOG);



    private static CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private static List<String> messageLog = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            log("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket);
                clients.add(handler);

                new Thread(handler).start();
            }

        } catch (IOException e) {
            log("Server Error: " + e.getMessage());
        }
    }

    public static synchronized void log(String msg) {
        try {
            String ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String entry = "[" + ts + "] " + msg;

            System.out.println(entry);
            messageLog.add(entry);

            try (PrintWriter pw = new PrintWriter(new FileWriter(LOG_FILE, true))) {
                pw.println(entry);
            }
        } catch (IOException e) {
            System.out.println("Logging failed: " + e.getMessage());
        }
    }

    public static void broadcast(String msg) {
        for (ClientHandler ch : clients) {
            ch.send(msg);
        }
    }

    public static void broadcastAlert(String msg) {
        String alert = "ALERT: " + msg;
        log("Alert broadcasted: " + msg);
        audit.log("alert", "server", "BROADCAST_ALERT", true, "text=" + msg);
        broadcast(alert);
    }

    public static String getUserList() {
        List<String> names = new ArrayList<>();
        for (ClientHandler ch : clients) {
            if (ch.username != null) {
                names.add(ch.username);
            }
        }
        return String.join(", ", names);
    }

    public static int getLogCount() {
        return messageLog.size();
    }

    public static void removeClient(ClientHandler ch) {
        clients.remove(ch);
    }

    // ==========================
    //  Client Handler Thread
    // ==========================
    static class ClientHandler implements Runnable {

        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        public String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Ask username
                out.println("Enter username:");
                username = in.readLine();

                if (username == null || username.trim().isEmpty()) {
                    username = "User" + new Random().nextInt(999);
                }

                String addr = String.valueOf(socket.getRemoteSocketAddress());
                log(username + " connected.");
                audit.log("client_connect", username, "CONNECT", true, "addr=" + addr);
                
                broadcast(username + " has joined the chat.");

                String msg;
                while ((msg = in.readLine()) != null) {
                    handleMessage(msg.trim());
                }

            } catch (IOException e) {
                String name = (username == null) ? "unknown" : username;
                log("Connection lost: " + name);
                audit.log("error", name, "EXCEPTION", false, e.toString());
            } finally {
                disconnect();
            }
        }

        private void handleMessage(String msg) {
            if (msg.startsWith("/")) {
                handleCommand(msg);
                return;
            }

            String chatMsg = username + ": " + msg;
            log(chatMsg);
            audit.log("message", username, "SEND_MESSAGE", true, "len=" + msg.length());
            broadcast(chatMsg);
        }

        private void handleCommand(String cmd) {
            audit.log("command", username, "COMMAND", true, "cmd=" + cmd);

            if (cmd.equals("/users")) {
                out.println("Connected users: " + getUserList());
            }
            else if (cmd.equals("/log")) {
                out.println("Total logged messages: " + getLogCount());
            }
            else if (cmd.startsWith("/alert ")) {
                String text = cmd.substring(7).trim();
                if (!text.isEmpty()) {
                    broadcastAlert(text);
                }
            }
            else if (cmd.equals("/quit") || cmd.equals("/exit")) {
                out.println("Goodbye.");
                disconnect();
            }
            else {
                out.println("Unknown command: " + cmd);
            }
        }

        public void send(String msg) {
            out.println(msg);
        }

        private void disconnect() {
            try {
                String name = (username == null) ? "unknown" : username;
                String addr = (socket == null) ? "unknown" : String.valueOf(socket.getRemoteSocketAddress());

                broadcast(name + " has left the chat.");
                removeClient(this);
                
                audit.log("client_disconnect", name, "DISCONNECT", true, "addr=" + addr);
                
                if (socket != null) socket.close();

            } catch (IOException ignore) {}
        }
    }
}