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
 * Receiver Thread
 * Listens for incoming broadcasts and displays them.
 *
 * @author Saumitro Dasgupta [skylar]
 */
public class Receiver extends Thread {

    private static final int BUFFER_SIZE = 8192;
    private DatagramSocket socket;
    private boolean isRunning = true;

    public Receiver(DatagramSocket socket)
            throws SocketException {
        this.socket = socket;

    }

    public void terminate() {
        isRunning = false;
        socket.close();

    }

    /**
     * Display a received broadcast message
     *
     * @param cmd The received broadcast command string
     */
    private void displayBroadcastMessage(String cmd) {

        Message msg = BroadcastProtocol.parseBroadcastMessage(cmd);

        if (msg == null) {
            return; //Malformed command received.
        }
        String from =
                "< Message from "
                + msg.srcAddress.getAddress().getHostAddress() + ":"
                + Integer.toString(msg.srcAddress.getPort()) + " >";


        System.out.println("\n");
        System.out.println(from);
        System.out.println(msg.body);
        System.out.println("\n");

        //Redisplay the user input prompt.
        System.out.print(Transmitter.userPrompt);

    }

    /**
     *
     * Listen for incoming messages from the server.
     *
     */
    @Override
    public void run() {

        byte[] buffer = new byte[BUFFER_SIZE];

        while (isRunning) {

            DatagramPacket packet = new DatagramPacket(buffer, BUFFER_SIZE);

            try {

                socket.receive(packet);

                String s = new String(packet.getData(), 0, packet.getLength());

                if (s.startsWith(BroadcastProtocol.CAST_MESSAGE)) {
                    displayBroadcastMessage(s);
                }


            } catch (IOException ex) {

                if(isRunning) System.err.println(ex);

            }

        }

    }
}
