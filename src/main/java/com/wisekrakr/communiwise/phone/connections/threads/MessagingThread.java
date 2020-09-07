package com.wisekrakr.communiwise.phone.connections.threads;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.Arrays;

public class MessagingThread {

    //Starts the client and connects to the specific server:port
    public static void startClient(String serverName, int serverPort) throws UnknownHostException, IOException {
        System.out.println("Trying to connect to host: " + serverName + ": " + serverPort);
        Socket socket = new Socket(serverName, serverPort);
        System.out.println("Sending open session message.");

        //Make the socket and get the I/O streams.
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        OutputStream clientStream = socket.getOutputStream();
        InputStream serverStream = socket.getInputStream();


        //Spins up a thread for reading from input and sending formatted messages to the server
        Thread sendMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                String send;
                try {
                    while (true) {
                        //These functions will only return something useful if we have the respective setting turned on.
                        send = input.readLine();

                        //todo Format the message

                        int formattedMessage = 0;
                        clientStream.write(formattedMessage);


                    }
                } catch (Throwable e) {
                    System.out.println("Unexpected error: sending message failed " + e);
                }
            }
        });

        //Spins up a thread for reading from the input socket stream and printing to console.
        Thread readMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        byte[] msg = new byte[16 * 1024];
                        int count = serverStream.read(msg);
                        msg = Arrays.copyOf(msg, count);
//                        String message = communication.handleMessage(msg,
//                                Paths.get("client_private", "publicServer.der"), crypto, key, security);
//                        System.out.println("server: " + message);

                    }
                } catch (Throwable e) {
                    System.out.println("Unexpected error: reading message failed " + e);
                }
            }
        });

        sendMessage.start();
        readMessage.start();
    }

    //Clean up the connection with the server.
    public static void disconnect(BufferedReader input,  OutputStream clientStream,  Socket socket) {
        try {
            input.close();
            clientStream.close();
            socket.close();
        } catch (Throwable e) {
            System.out.println("Unexpected error: disconnecting messenger failed " + e);
        }
    }

}
