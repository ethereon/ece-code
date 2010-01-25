/*
 *
 * Copyright (C) 2010 Saumitro Dasgupta.
 *
 * This code is made available under the MIT License.
 * <http://www.opensource.org/licenses/mit-license.html>
 *
 */
package broadcast.protocol;

/**
 *
 * Broadcast Protocol
 *
 * @author Saumitro Dasgupta [skylar]
 * @version 0.1
 */
public class BroadcastProtocol {

    /* Command Type Identifiers */
    public static final String SUBSCRIBE = "GREETING";
    public static final String CLIENT_MESSAGE = "MESSAGE";
    public static final String CAST_MESSAGE = "INCOMING";
    /* Command String Delimiter */
    public static final String DELIMITER = " ";
    public static final String[] commands = {
        SUBSCRIBE,
        CLIENT_MESSAGE,
        CAST_MESSAGE
    };

    /**
     * Check if a string is an instance of a particular type of command
     *
     * @param cmd The command string to analyze
     * @param type The command type to compare against
     * @return true if cmd is an instance of type
     */
    public static boolean isCommandOfType(String cmd, String type) {

        if (type.equals(SUBSCRIBE)) {
            return cmd.equals(SUBSCRIBE);
        } else {
            return cmd.startsWith(type + DELIMITER)
                    && cmd.length() > (type.length() + DELIMITER.length());
        }

    }

    /**
     * Get the command type represent by a string
     *
     * @param cmd The command string
     * @return Type identifier if cmd is a valid command string, else NULL
     */
    public static String getCommandType(String cmd) {

        for (String typeName : commands) {

            if (isCommandOfType(cmd, typeName)) {
                return typeName;
            }
        }

        return null;

    }

    /**
     *
     * Construct a Message object from a command string
     *
     * @param cmd A valid message command string
     * @return A populated Message Object if cmd is valid, else NULL
     */
    public static Message parseBroadcastMessage(String cmd) {

        String[] parts = cmd.split(BroadcastProtocol.DELIMITER);
        int c = parts.length;

        //Must contain, at very least, (INCOMING, msg0, ip, port)
        if (c < 4) {
            return null;
        }

        String senderIp = parts[c - 2];
        String senderPort = parts[c - 1];

        int delimLen = BroadcastProtocol.DELIMITER.length();

        int iS = BroadcastProtocol.CAST_MESSAGE.length() + delimLen;
        int iE = cmd.length()
                - (senderIp.length() + senderPort.length() + 2 * delimLen);

        String body = cmd.substring(iS, iE);

        return new Message(body, senderIp, Integer.parseInt(senderPort));


    }

    /**
     * Extract the body of the message from a client message command
     *
     * @param cmd A valid message command string
     * @return The body of the message if cmd is valid, else NULL
     */
    public static String parseClientMessage(String cmd) {

        int cmdLen = CLIENT_MESSAGE.length();

        //Verify that a message string exists
        if (cmd.length() < (cmdLen + 2)) {
            return null;
        }

        return cmd.substring(cmdLen + DELIMITER.length());

    }

    /**
     * Creates a broadcast command string for a given message
     *
     * @param msg The message to broadcast
     * @return A broadcast command string
     */
    public static String generateBroadcastMessageCommand(Message msg) {

        String cmd = CAST_MESSAGE + DELIMITER;

        cmd += msg.body;
        cmd += DELIMITER + msg.srcAddress.getAddress().getHostAddress();
        cmd += DELIMITER + Integer.toString(msg.srcAddress.getPort());

        return cmd;
    }
}
