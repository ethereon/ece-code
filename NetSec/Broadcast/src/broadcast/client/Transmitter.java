/*
 *
 * Copyright (C) 2010 Saumitro Dasgupta.
 *
 * This code is made available under the MIT License.
 * <http://www.opensource.org/licenses/mit-license.html>
 *
 */
package broadcast.client;

import java.io.*;
import java.net.*;

import broadcast.protocol.*;

/**
 *
 * Transmitter thread
 * Reads & validates commands from stdin,  and sends them to the server.
 *
 * @author Saumitro Dasgupta [skylar]
 */
public class Transmitter extends Thread {

    /* Client commands */
    private static final String CMD_MESSAGE = "MESSAGE";
    private static final String CMD_QUIT = "q";
    private static final String DELIMITER = " ";

    private static String userPrompt = ">> ";
    private DatagramSocket socket;
    
    public Transmitter(DatagramSocket socket) {

        this.socket = socket;

    }

    /**
     * Displays the command line entry prompt for user input
     */
    public static void displayUserPrompt() {

        System.out.print(Transmitter.userPrompt);
    }
    

    /**
     * Send the command to the broadcast server.
     * 
     * @param cmd The command string to send.
     */
    private void transmitCommand(String cmd) {

        if(cmd==null) return;
        
        byte[] cmdBytes = cmd.getBytes();

        try {

            DatagramPacket msgPacket =
                    new DatagramPacket(cmdBytes,
                    cmdBytes.length,
                    socket.getRemoteSocketAddress());

            socket.send(msgPacket);

        } catch (IOException ex) {

            System.err.println(ex);

        }

    }

    /**
     * Subscribe to the broacast server
     */
    private void subscribe() {

        transmitCommand(BroadcastProtocol.getSubscriptionCommand());

    }

    /**
     * Parse the command string and send the message to the broadcast server
     *
     * @param cmd A valid command string
     */
    private void sendMessageForBroadcast(String cmd) {

        if(cmd.length()<CMD_MESSAGE.length()+DELIMITER.length()+1) {

            System.err.println("No message specified.");
            return;
        }

        String msg = cmd.substring(CMD_MESSAGE.length()+DELIMITER.length());
        transmitCommand(BroadcastProtocol.generateBroadcastRequest(msg));

    }

    /**
     * Handle user input
     * @param in string entered by the user
     */
    private void handleInput(String in) {

        if(in.startsWith(CMD_MESSAGE+DELIMITER))
            sendMessageForBroadcast(in);
        else
            System.err.println("Invalid command string.");

    }


    @Override
    public void run() {

        subscribe();

        try {

            BufferedReader in =
                    new BufferedReader(new InputStreamReader(System.in));


            while (true) {

                displayUserPrompt();
                String lineIn = in.readLine();

                if (lineIn == null || lineIn.equals(CMD_QUIT)) break;
                
                handleInput(lineIn);

            }

        } catch (IOException ex) {

            System.err.println(ex);

        }

    }
}
