/*
 *
 * Copyright (C) 2010 Saumitro Dasgupta.
 *
 * This code is made available under the MIT License.
 * <http://www.opensource.org/licenses/mit-license.html>
 *
 */

package cryptonite;

/**
 *
 * Class for managing supported cryptographic algorithms
 *
 * @author Saumitro Dasgupta
 */
public class Algorithms {

    public static final String AES_ALGORITHM= "AES";
    public static final String AES_DEFAULT_MODE = "AES/CTR/NoPadding";

    public static final String RSA_ALGORITHM = "RSA";
    public static final String RSA_DEFAULT_MODE = "RSA/None/NoPadding";

    public static final String SHA_RSA_DEFAULT_MODE = "SHA256WithRSAEncryption";


    public static final String[] symmetricMethods = {

        AES_DEFAULT_MODE
    };

    public static final String[] publicKeyMethods = {

        RSA_DEFAULT_MODE

    };

    public static final String[] signatureMethods = {

        SHA_RSA_DEFAULT_MODE

    };

    private static boolean isMemberOfArray(String[] arr, String value) {

        for (String aValue: arr) {
            if(aValue.equals(value))
                return true;
        }

        return false;
    }

    public static boolean isSupportedSymmetricMethod(String method)
    {
        return isMemberOfArray(symmetricMethods, method);
    }

    public static boolean isSupportedPublicKeyMethod(String method)
    {
        return isMemberOfArray(publicKeyMethods, method);
    }

    public static boolean isSupportedSignatureMethod(String method)
    {
        return isMemberOfArray(signatureMethods, method);
    }


}
