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
import java.util.*;

import broadcast.protocol.*;

/**
 *
 * A UDP Message Broadcast Server
 *
 * @author Saumitro Dasgupta [skylar]
 * @version 0.1
 *
 */
public class BroadcastServer extends AbstractUDPServer {

    private final static int PACKET_LENGTH = 1024;
    private Set<InetSocketAddress> subscribers;

    public BroadcastServer(int port)
            throws SocketException {

        super(PACKET_LENGTH, port);
        subscribers = new HashSet<InetSocketAddress>();

    }

    /**
     * Add a client to the list of subscribers
     * @param sa Client's socket address
     */
    private void addSubscriber(InetSocketAddress sa) {

        subscribers.add(sa);

    }

    /**
     * Broadcast the specified message to each subscribing client
     * (including the one that sent the message)
     *
     * @param sa IP Address and Port of the client sending the message
     * @param cmd The message command string (MESSAGE msg...)
     */
    private void broadcastMessage(InetSocketAddress sa, String cmd) {


        //Only registered subscribers are permitted to broadcast a message.
        //Verify that the sender is a registered subscriber.
        if (!subscribers.contains(sa)) {
            return;
        }

        //Construct a broadcast message command string.
        String body = BroadcastProtocol.parseClientMessage(cmd);
        Message msg = new Message(body, sa);
        String castCmd = BroadcastProtocol.generateBroadcastMessageCommand(msg);

        byte[] data = castCmd.getBytes();

        //Send message to each subscriber.
        for (InetSocketAddress client : subscribers) {

            try {


                DatagramPacket packet =
                        new DatagramPacket(
                        data,
                        data.length,
                        client.getAddress(),
                        client.getPort());

                socket.send(packet);

            } catch (IOException ex) {

                System.err.println(ex);

            }

        }

    }

    /**
     * Respond to the incoming request.
     *
     * @param packet
     */
    public void respond(DatagramPacket packet) {

        InetSocketAddress sa = (InetSocketAddress) packet.getSocketAddress();
        String cmd = new String(packet.getData(), 0, packet.getLength());

        String cmdType = BroadcastProtocol.getCommandType(cmd);

        if (cmdType == null) {
            return;
        }


        if (cmdType.equals(BroadcastProtocol.SUBSCRIBE)) {
            addSubscriber(sa);
        } else if (cmdType.equals(BroadcastProtocol.CLIENT_MESSAGE)) {
            broadcastMessage(sa, cmd);
        }

    }

    /**
     *
     * Creates a new broadcast server and binds it the port
     * specified in the command line.
     *
     * @param args Command line args (expected: args[0] = port)
     *
     */
    public static void main(String[] args) {


        if (args.length != 1) {

            System.out.println("usage: BroadcastServer port");
            return;
        }

        try {

            int port = Integer.parseInt(args[0]);

            final String msg = "Starting Broadcast Server on port " + args[0];

            System.out.println(msg);

            BroadcastServer server = new BroadcastServer(port);
            server.run();


        } catch (NumberFormatException ex) {

            System.err.println("Invalid port specified.");
            return;

        } catch (SocketException ex) {

            System.err.println(ex);

        }

    }
}
