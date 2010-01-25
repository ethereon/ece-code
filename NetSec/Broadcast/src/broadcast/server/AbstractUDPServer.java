/*
 *
 * Copyright (C) 2010 Saumitro Dasgupta.
 *
 * This code is made available under the MIT License.
 * <http://www.opensource.org/licenses/mit-license.html>
 *
 */
package broadcast.server;

import java.io.*;
import java.net.*;

/**
 *
 * An abstract UDP Server Class
 *
 * @author Saumitro Dasgupta [skylar]
 * @version 0.1
 *
 */
public abstract class AbstractUDPServer extends Thread {

    protected int bufferLength;
    protected DatagramSocket socket;

    public AbstractUDPServer(int bufferLength, int port)
            throws SocketException {
        this.bufferLength = bufferLength;
        this.socket = new DatagramSocket(port);

    }

    /**
     * 
     * Start receiving data and forwarding requests
     * to the {@link #respond} method.
     */
    @Override
    public void run() {


        byte[] buffer = new byte[bufferLength];

        while (true) {

            DatagramPacket inbound = new DatagramPacket(buffer, bufferLength);

            try {

                socket.receive(inbound);
                this.respond(inbound);

            } catch (IOException ex) {

                System.err.println(ex);
            }

        }

    }

    /**
     *
     * Abstract method that is called whenever a new packet
     * is received. To be overriden by the subclass.
     *
     * @param request The received packet
     */
    public abstract void respond(DatagramPacket request);
}
