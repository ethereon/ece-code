/*
 *
 * Copyright (C) 2010 Saumitro Dasgupta.
 *
 * This code is made available under the MIT License.
 * <http://www.opensource.org/licenses/mit-license.html>
 *
 */

package cryptonite;

import java.io.Serializable;

/**
 *
 * A class for representing a signed encrypted file, along with
 * its characteristics.
 *
 * @author Saumitro Dasgupta
 */
public class EncryptedFile implements Serializable {

    static final long serialVersionUID = 1654543777095207693L;

    /** The method used for encrypting the content */
    private String symmetricMethod;

    /** The method used for encrypting the secret key */
    private String publicKeyMethod;

    /** The method used for generating the signature */
    private String signatureMethod;

    /** The encrypted secret key */
    private byte[] encryptedKey;

    /** The digital signature */
    private byte[] signature;

    /** The encrypted original file contents */
    private byte[] encryptedData;


    public void setEncrypedData(byte[] data) {

        this.encryptedData = data;
    }

    public void setSignature(byte[] signature) {

        this.signature = signature;
    }

    public void setEncryptedKey(byte[] key) {

        this.encryptedKey = key;
    }

    public byte[] getEncryptedData() {

        return this.encryptedData;
    }

    public byte[] getEncryptedKey() {

        return encryptedKey;

    }

    public byte[] getSignature() {

        return this.signature;
    }


    public String getSymmetricMethod() {

        return this.symmetricMethod;
    }

    public void setSymmetricMethod(String Method) {

        this.symmetricMethod = Method;
    }

    public String getPublicKeyMethod() {

        return this.publicKeyMethod;
    }

    public void setPublicKeyMethod(String Method) {

        this.publicKeyMethod = Method;
    }

    public String getSignatureMethod() {

        return this.signatureMethod;
    }

    public void setSignatureMethod(String Method) {

        this.signatureMethod = Method;
    }

}
