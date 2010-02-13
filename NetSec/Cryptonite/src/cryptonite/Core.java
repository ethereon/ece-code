/*
 *
 * Copyright (C) 2010 Saumitro Dasgupta.
 *
 * This code is made available under the MIT License.
 * <http://www.opensource.org/licenses/mit-license.html>
 *
 */

package cryptonite;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * Central class which facilitates the process of encrypting,
 * decrypting and authenticating files.
 *
 * @author Saumitro Dasgupta
 */
public class Core {

    public static void trace(String msg)
    {

        System.out.println("[Cryptonite]\t" + msg);

    }


    /**
     * Check if the algorithms/methods used in an encrypted file
     * are compatible with the current implementation.
     *
     * @param encryptedFile The encrypted file to check
     * @return true if the given instance is compatible
     */
    public static boolean isCompatibleFile(EncryptedFile encryptedFile)
    {

        if (!Algorithms.isSupportedPublicKeyMethod(encryptedFile.getPublicKeyMethod())) {

            System.out.println("Unsupported public key method.");
            return false;
        }

        if (!Algorithms.isSupportedSymmetricMethod(encryptedFile.getSymmetricMethod())) {

            System.out.println("Unsupported private key method.");
                return false;
        }

        if (!Algorithms.isSupportedSignatureMethod(encryptedFile.getSignatureMethod())) {

            System.out.println("Unsupported signature method.");
            return false;

        }

        return true;

    }

    /**
     *
     * Encrypt and sign a file using the given keys
     *
     * Currently, only the X.509 PEM file format is supported.
     * (Such as the ones generated by OpenSSL)
     *
     * @param infile The file to encrypt
     * @param outfile The destination file ( encrypted)
     * @param privateKeyFile File containing the private key ( For signing )
     * @param publicKeyFile File containing the public key ( For encryption )
     */
    public static void encryptFile(String infile,
                                   String outfile,
                                   String privateKeyFile,
                                   String publicKeyFile)

    {


        try {

            //Read in the keys

            KeyPair keyPair = Util.keyPairFromPemFile(privateKeyFile, "");
            PublicKey publicKey = Util.publicKeyFromPemFile(publicKeyFile, "");

            if(keyPair==null) {

                System.err.println("Unable to use given private key file.");
                return;
            }

            if(publicKey==null) {

                System.err.println("Unable to use given public key file.");
                return;
            }

            //Generate a random secret key

            trace("Generating random secret key");
            SecretKey secretKey = SymmetricEndec.generateRandomKey(Algorithms.AES_ALGORITHM);

            EncryptedFile encryptedFile = createEncryptedFile(infile,
                                                              keyPair.getPrivate(),
                                                              publicKey,
                                                              secretKey,
                                                              Algorithms.AES_DEFAULT_MODE,
                                                              Algorithms.RSA_DEFAULT_MODE,
                                                              Algorithms.SHA_RSA_DEFAULT_MODE);

            FileOutputStream fos = new FileOutputStream(outfile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            trace("Writing output file");
            oos.writeObject(encryptedFile);

            oos.close();

            trace("Done");


        }
        catch (NoSuchAlgorithmException ex) {

            System.err.println("Unable to generate key for symmetric algorithm.");
        }
        catch (FileNotFoundException exception) {

            exception.printStackTrace();

        }
        catch (IOException exception) {

            exception.printStackTrace();
        }

    }

    /**
     * Decrypt a file and verify its signature
     *
     * @param infile The encrypted file
     * @param outfile The destination output file (decrypted)
     * @param privateKeyFile The file containing the private key ( for decryption )
     * @param publicKeyFile The file containing the public key ( for signature verification)
     */

    public static void decryptFile(String infile,
                                   String outfile,
                                   String privateKeyFile,
                                   String publicKeyFile)
    {

        try {

            //Read in the keys

            KeyPair keyPair = Util.keyPairFromPemFile(privateKeyFile, "");
            PublicKey publicKey = Util.publicKeyFromPemFile(publicKeyFile, "");

            if(keyPair==null) {

                System.err.println("Unable to use given private key file.");
                return;
            }

            if (publicKey == null) {

                System.err.println("Unable to use given public key file.");
                return;
            }

            FileInputStream fis = new FileInputStream(infile);
            ObjectInputStream in = new ObjectInputStream(fis);

            trace("Reading file");
            EncryptedFile encryptedFile = (EncryptedFile)in.readObject();

            //Check if the file is compatible with the current implementation

            if(!isCompatibleFile(encryptedFile))
                return;
            
            //Decrypt the secret key
            trace("Recovering secret key");
            byte[] keyBytes = PublicKeyEndec.decrypt(encryptedFile.getEncryptedKey(),
                                                     keyPair.getPrivate(),
                                                     encryptedFile.getPublicKeyMethod());


            String secretKeyAlgorithm = Util.extractAlgorithmName(encryptedFile.getSymmetricMethod());

            SecretKey secretKey = new SecretKeySpec(keyBytes, secretKeyAlgorithm);


            //Decrypt the contents of the file
            trace("Decrypting");
            byte[] message = SymmetricEndec.decrypt(encryptedFile.getEncryptedData(),
                                                    secretKey,
                                                    encryptedFile.getSymmetricMethod());

            //Verify the digital signature
            trace("Verifying digital signature");
            boolean isAuthentic = SignatureAuthority.verifySignature(encryptedFile.getSignature(),
                                                                     message,
                                                                     publicKey,
                                                                     encryptedFile.getSignatureMethod());

            if(isAuthentic) {

                trace("Signature verification successful");

            } else {

                trace("Signature verification Failed!");
            }

            //Write the decrypt file

            trace("Writing decrypted file");
            FileOutputStream fos = new FileOutputStream(outfile);
            fos.write(message);
            fos.close();

            trace("Done");

        }
        catch(ClassNotFoundException exception) {

            System.out.println("Incompatible encrypted class version.");

        }
        catch (FileNotFoundException exception) {

            exception.printStackTrace();

        }
        catch (IOException exception) {

            exception.printStackTrace();
        }



    }

    /**
     *
     * Create an EncryptedFile instance for the given parameters
     *
     * @param filename The file to encrypt
     * @param signersKey The key with which the message will be signed
     * @param receiversKey The key to use for encrypting the secret key
     * @param symmetricKey The secret key used for encrypting the file
     * @param symmetricMethod The algorithm to use for symmetric encryption
     * @param publicKeyMethod The algorithm to use for public key encryption
     * @param signatureMethod The algorithm to use for signing the file
     * @return A populated EncryptedFile instance
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static EncryptedFile createEncryptedFile(String filename,
                                                    PrivateKey signersKey,
                                                    PublicKey receiversKey,
                                                    SecretKey symmetricKey,
                                                    String symmetricMethod,
                                                    String publicKeyMethod,
                                                    String signatureMethod)

            throws FileNotFoundException,
                   IOException

    {

        trace("Reading input file");
        byte[] fileBytes = Util.getFileContentsAsByteArray(filename);

        trace("Encrypting file");
        byte[] cipherBytes = SymmetricEndec.encrypt(fileBytes, symmetricKey, symmetricMethod);

        byte[] secretKeyBytes = symmetricKey.getEncoded();

        trace("Encrypting secret key");
        byte[] encryptedKey = PublicKeyEndec.encrypt(secretKeyBytes, receiversKey, publicKeyMethod);

        trace("Generating signature");
        byte[] signature = SignatureAuthority.generateSignature(fileBytes, signersKey, signatureMethod);


        EncryptedFile encryptedFile = new EncryptedFile();

        encryptedFile.setPublicKeyMethod(publicKeyMethod);
        encryptedFile.setSymmetricMethod(symmetricMethod);
        encryptedFile.setSignatureMethod(signatureMethod);
        encryptedFile.setEncrypedData(cipherBytes);
        encryptedFile.setEncryptedKey(encryptedKey);
        encryptedFile.setSignature(signature);

        return encryptedFile;


    }



}