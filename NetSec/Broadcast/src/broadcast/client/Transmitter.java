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

    public static final String QUIT_KEY = "q";
    public static String userPrompt = ">> ";
    private DatagramSocket socket;

    public Transmitter(DatagramSocket socket) {

        this.socket = socket;

    }

    /**
     * Send the command to the broadcast server.
     * 
     * @param cmd The command string to send.
     */
    private void transmitCommand(String cmd) {

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

    private void handleInput(String in) {

        String cmdType = BroadcastProtocol.getCommandType(in);

        if (cmdType == null) {
            System.err.println("Invalid command string.");
        } else if (cmdType.equals(BroadcastProtocol.SUBSCRIBE)
                || cmdType.equals(BroadcastProtocol.CLIENT_MESSAGE)) {
            transmitCommand(in);
        }

    }

    @Override
    public void run() {

        try {

            BufferedReader in =
                    new BufferedReader(new InputStreamReader(System.in));


            while (true) {

                System.out.print(userPrompt);
                String lineIn = in.readLine();

                if (lineIn == null || lineIn.equals(QUIT_KEY)) break;
                
                handleInput(lineIn);

            }

        } catch (IOException ex) {

            System.err.println(ex);

        }

    }
}
