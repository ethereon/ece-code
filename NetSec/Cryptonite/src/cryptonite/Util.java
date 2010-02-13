/*
 *
 * Copyright (C) 2010 Saumitro Dasgupta.
 *
 * This code is made available under the MIT License.
 * <http://www.opensource.org/licenses/mit-license.html>
 *
 */

package cryptonite;

import java.io.*;
import java.security.*;
import org.bouncycastle.openssl.PEMReader;



/**
 *
 * Common Cryptograpghic Utility Methods
 *
 * @author Saumitro Dasgupta
 */

public class Util {

    /**
     * Extract the Algorithm name from a method string
     * @param methodName
     * @return The algorithm name
     */
    public static String extractAlgorithmName(String methodName) {

        return methodName.split("/")[0];

    }

    /**
     *
     * Read in KeyPair from a PEM file
     *
     * @param filename The file containing the Private + Public key
     * @param password Password used for encrypting the file (if any)
     * @return A KeyPair instance containing the keys from the file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static  KeyPair keyPairFromPemFile(String filename, String password)
            throws FileNotFoundException,
                   IOException
    {

        if(password==null) password="";

        FileReader fileReader = new FileReader(filename);
        PEMReader pemReader = new PEMReader(fileReader,
                                            new DefaultPasswordFinder(password));

        return (KeyPair)pemReader.readObject();

    }

    /**
     *
     * Read in a public key from a PEM file
     *
     * @param filename The file containing the public key
     * @param password Password used for encrypting the file (if any)
     * @return A PublicKey instance containing the key from the file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static PublicKey publicKeyFromPemFile(String filename, String password)
            throws FileNotFoundException,
                   IOException
    {

        FileReader fileReader = new FileReader(filename);
        PEMReader pemReader = new PEMReader(fileReader,
                                            new DefaultPasswordFinder(password));

        return (PublicKey)pemReader.readObject();

    }

    /**
     *
     * Read in the contents of a file into a byte array
     *
     * @param filename The file to read
     * @return A byte array containing the contents of the file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static byte[] getFileContentsAsByteArray(String filename)
            throws FileNotFoundException,
                   IOException
    {

        File file = new File(filename);
        FileInputStream fis = new FileInputStream(file);

        int len = (int)file.length();

        byte[] bytes = new byte[len];

        int bytesRead = 0;
        int offset=0;

        while(offset < len) {

            bytesRead = fis.read(bytes,offset,len-offset);

            if(bytesRead==-1) break;

            offset+=bytesRead;

        }

        if(offset < len)
            throw new IOException("Unable to read in all the bytes");

        fis.close();

        return bytes;


    }


}
