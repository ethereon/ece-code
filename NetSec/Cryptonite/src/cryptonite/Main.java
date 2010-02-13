/*
 *
 * Copyright (C) 2010 Saumitro Dasgupta.
 *
 * This code is made available under the MIT License.
 * <http://www.opensource.org/licenses/mit-license.html>
 *
 */

package cryptonite;


import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * Cryptonite
 *
 * @author Saumitro Dasgupta
 */
public class Main {

    private static final String BOUNCY_CASTLE_PROVIDER = "BC";

    /**
     * Initializes the Bouncy Castle Provider
     * @return true if the initialization suceeded
     */
    static boolean initializeProvider() {

        Security.addProvider(new BouncyCastleProvider());
        return Security.getProvider(BOUNCY_CASTLE_PROVIDER) != null;
    }


    /**
     * Displays the command line syntax for Cryptonite
     */
    static void displayUsage() {

        System.out.print("\n");
        System.out.println("usage: cryptonite -<command> <command parameters> ");
        System.out.println("\n[ Commands ]\n");
        System.out.println("\t-e : Encrypt file");
        System.out.println("\t\t parameters : publicKeyBPath privateKeyAPath plaintextFile enryptedOutputFile\n");
        System.out.println("\t-d : Decrypt file");
        System.out.println("\t\t parameters : privateKeyBPath publicKeyAPath encryptedFile outputPlaintextFile\n");
        System.out.println("[ Examples ]\n");
        System.out.println("cryptonite -e Bob.pub Alice.pem alchemy.tar encrypted.tar");
        System.out.println("cryptonite -d Bob.pem Alice.pub encrypted.tar alchemy.tar");
        System.out.print("\n");
    }

    /**
     *
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {

        if(args.length!=5) {

            displayUsage();
            return;

        }

        initializeProvider();
        
        if(args[0].equals("-e")) {

            Core.encryptFile(args[3], args[4], args[2], args[1]);
            
        } else if (args[0].equals("-d")) {

            Core.decryptFile(args[3], args[4], args[1], args[2]);

        }





    }
}
