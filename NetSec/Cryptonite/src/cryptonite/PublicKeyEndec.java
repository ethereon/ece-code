/*
 *
 * Copyright (C) 2010 Saumitro Dasgupta.
 *
 * This code is made available under the MIT License.
 * <http://www.opensource.org/licenses/mit-license.html>
 *
 */

package cryptonite;


import java.security.*;
import javax.crypto.Cipher;



/**
 *
 * Class for handling public key encryption / decryption
 *
 * @author Saumitro Dasgupta
 */


public class PublicKeyEndec {
    

    /**
     *
     * Method capable of encrypting / decrypting data
     *
     * @param op Cipher mode ( Encrypt / Decrypt)
     * @param input Plaintext / Ciphertext
     * @param key Public / Private key
     * @param method Encryption Algorithm + Method
     * @return Ciphertext / Plaintext
     */

    private static byte[] process(int op, byte[] input, Key key, String method)
    {
        try {
            Cipher cipher = Cipher.getInstance(method);
            cipher.init(op, key);
            byte[] output = cipher.doFinal(input);
            return output;
        }
        catch (InvalidKeyException exception) {

            throw new RuntimeException("Invalid key provided");
        }
        catch (GeneralSecurityException exception) {

            throw new RuntimeException("Security exception encountered during cipher initialization");
        }
    }

    /**
     *
     * Encrypt the given byte array
     *
     * @param plainText
     * @param key
     * @param method A valid public key algorithm string
     * @return Byte array containing the Ciphertext
     */
    public static byte[] encrypt(byte[] plainText, Key key, String method)
    {

        return process(Cipher.ENCRYPT_MODE, plainText, key, method);

    }

    /**
     *
     * Decrypt the given byte array
     *
     * @param cipherText
     * @param key
     * @param method A valid public key algorithm string
     * @return Byte array containing the Plaintext
     */
    public static byte[] decrypt(byte[] cipherText, Key key, String method)
    {

        return process(Cipher.DECRYPT_MODE, cipherText, key, method);

    }


}
