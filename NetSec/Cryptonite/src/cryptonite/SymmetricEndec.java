/*
 *
 * Copyright (C) 2010 Saumitro Dasgupta.
 *
 * This code is made available under the MIT License.
 * <http://www.opensource.org/licenses/mit-license.html>
 *
 */

package cryptonite;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 *
 * Class for handling symmetric encryption / decryption
 *
 * @author Saumitro Dasgupta
 */
public class SymmetricEndec {

    private static final byte[] IV = new byte[]{
        
        0x00, 0x00, 0x00, 0x01, 0x04, 0x05, 0x06, 0x07,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01
                
    };

    /**
     *
     * Generate a random secret key
     *
     * @param algorithm The target algorithm for the key
     * @return A SecretKey instance containing a randomly generated key
     * @throws NoSuchAlgorithmException
     */
    public static SecretKey generateRandomKey(String algorithm)
            throws NoSuchAlgorithmException
    {
        
        KeyGenerator keygen = KeyGenerator.getInstance(algorithm);
        return keygen.generateKey();
    }

    /**
     *
     * Common method capable of encrypting / decrypting data
     *
     * @param op Cipher mode ( Encrypt / Decrypt)
     * @param input Plaintext / Ciphertext
     * @param key The secret key
     * @param method The algorithm to use
     * @return Ciphertext / Plaintext
     */
    private static byte[] process(int op, byte[] input, SecretKey key, String method)
    {

        try {

            Cipher cipher = Cipher.getInstance(method);
            cipher.init(op, key, new IvParameterSpec(IV));
            byte[] output = new byte[cipher.getOutputSize(input.length)];
            int len = cipher.update(input, 0, input.length, output, 0);
            len += cipher.doFinal(output, len);

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
     * Encrypt a byte array
     *
     * @param plainText
     * @param key
     * @param method A valid cipher algorithm string ( eg: "AES/ECB/NoPadding" )
     * @return Byte array containing the Ciphertext
     */
    public static byte[] encrypt(byte[] plainText, SecretKey key, String method) {

        return process(Cipher.ENCRYPT_MODE, plainText, key, method);

    }

    /**
     *
     * Decrypt a byte array
     *
     * @param cipherText
     * @param key
     * @param method A valid cipher algorithm string ( eg: "AES/ECB/NoPadding" )
     * @return Byte array containing the Plaintext
     */
    public static byte[] decrypt(byte[] cipherText, SecretKey key, String method) {

        return process(Cipher.DECRYPT_MODE, cipherText, key, method);

    }

}
