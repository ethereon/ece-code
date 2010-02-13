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


/**
 *
 * Class for generating signatures / authenticating signatures
 *
 * @author Saumitro Dasgupta
 */
public class SignatureAuthority {


    /**
     *
     * Generate a digital signature
     *
     * @param message The message to sign
     * @param key The private key to use for signing
     * @param method The algorithm to use for generating the signature
     * @return A byte array containing the digital signature
     */
    public static byte[] generateSignature(byte[] message, PrivateKey key, String method)
    {

        try {

            Signature signer = Signature.getInstance(method);
            signer.initSign(key);
            signer.update(message);
            return signer.sign();

        }
        catch(GeneralSecurityException exception) {

            throw new RuntimeException("Exception encountered during signing.");
        }

    }

    /**
     *
     * Verify a digital signature
     *
     * @param signature The signature to verify
     * @param message The message to authenticate
     * @param key The signer's public key
     * @param method The method used for generating the signature
     * @return true if the signature is valid, false otherwise
     */
    public static boolean verifySignature(byte[] signature, byte[] message, PublicKey key, String method)
    {

        try {

            Signature verifier = Signature.getInstance(method);
            verifier.initVerify(key);
            verifier.update(message);

            return verifier.verify(signature);

        }
        catch(GeneralSecurityException exception) {

            throw new RuntimeException("Exception encountered during verification.");
        }


    }

}
