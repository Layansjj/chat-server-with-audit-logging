
package com.mycompany.client.server;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {

    private static final int PORT = 5000;
    private static final String HOST = "localhost";


    public static void main(String[] args) {

        try (Socket socket = new Socket(HOST, PORT)) {

            System.out.println("Connected to server.");

            BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out    = new PrintWriter(socket.getOutputStream(), true);
            
            Scanner console    = new Scanner(System.in);

            System.out.println(in.readLine()); // "Enter username:"

            System.out.print("> ");
            out.println(console.nextLine());

            Thread listener = new Thread(() -> {
                try {
                    String serverMsg;
                    while ((serverMsg = in.readLine()) != null) {
                        System.out.println(serverMsg);
                    }
                } catch (IOException e) {
                    System.out.println("Server disconnected.");
                }
            });

            listener.start();

            while (true) {
                String msg = console.nextLine();
                out.println(msg);

                if (msg.equals("/quit") || msg.equals("/exit")) {
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}