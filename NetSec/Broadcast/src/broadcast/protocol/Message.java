/*
 *
 * Copyright (C) 2010 Saumitro Dasgupta.
 *
 * This code is made available under the MIT License.
 * <http://www.opensource.org/licenses/mit-license.html>
 *
 */
package broadcast.protocol;

import java.net.*;

/**
 *
 * Message Class
 * Represents a message sent by a subscriber for broadcast
 *
 * @author Saumitro Dasgupta [skylar]
 */
public class Message {

    public String body;
    /** The address of the source client **/
    public InetSocketAddress srcAddress;

    public Message(String body, InetSocketAddress sa) {

        this.body = body;
        srcAddress = sa;

    }

    public Message(String body, String addr, int port) {

        this(body, new InetSocketAddress(addr, port));

    }
}
