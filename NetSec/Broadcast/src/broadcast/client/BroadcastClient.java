/*
 *
 * Copyright (C) 2010 Saumitro Dasgupta.
 *
 * This code is made available under the MIT License.
 * <http://www.opensource.org/licenses/mit-license.html>
 *
 */
package broadcast.client;

import java.net.*;

/**
 *
 * A client that communicates with the broadcast server.
 *
 * @author Saumitro Dasgupta [skylar]
 */
public class BroadcastClient {

    /**
     * Displays the client preamble message
     *
     * @param server Address of the broadcast server
     * @param port Port of the broadcast server
     */
    public static void displayPreamble(String server, String port) {

        String s = "\nBroadcast Client / ";

        s += "Connected to " + server + " on port " + port;
        s += "\nType \"q\" to quit.";
        s += "\n";

        System.out.println(s);
    }

    public static void main(String[] args) {

        if (args.length != 2) {

            System.out.println("usage: BroadcastClient serverAddr serverPort");
            return;
        }


        try {

            //Parse arguments
            InetAddress addr = InetAddress.getByName(args[0]);
            int port = Integer.parseInt(args[1]);

            //Open a socket to the server
            DatagramSocket socket = new DatagramSocket();
            socket.connect(addr, port);

            displayPreamble(addr.getHostAddress(), args[1]);

            Transmitter tx = new Transmitter(socket);
            Receiver rx = new Receiver(socket);

            //Start listening for incoming messages
            rx.start();
            //Read commands from stdin and send to the server.
            //Block until transmitter thread terminates.
            tx.run();
            //Transmitter thread ended. Kill the receiver thread.
            rx.terminate();


        } catch (UnknownHostException ex) {

            System.err.println(ex);

        } catch (NumberFormatException ex) {

            System.err.println("Invalid port specified.");
            return;

        } catch (SocketException ex) {

            System.err.println(ex);

        }

    }
}
