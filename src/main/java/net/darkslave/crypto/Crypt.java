/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.crypto;


import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;





public class Crypt {

    /**
     * Сгенерировать пару публичный / приватный ключи
     * @throws GeneralSecurityException
     */
    public static KeyPair generateKeyPair(String algorithm, int size) throws GeneralSecurityException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);

        generator.initialize(size);

        return generator.generateKeyPair();
    }


    /**
     * Сгенерировать ключи
     *
     * Размеры ключей для алгоритмов:
     * AES/CBC/NoPadding (128)
     * AES/CBC/PKCS5Padding (128)
     * AES/ECB/NoPadding (128)
     * AES/ECB/PKCS5Padding (128)
     * DES/CBC/NoPadding (56)
     * DES/CBC/PKCS5Padding (56)
     * DES/ECB/NoPadding (56)
     * DES/ECB/PKCS5Padding (56)
     * DESede/CBC/NoPadding (168)
     * DESede/CBC/PKCS5Padding (168)
     * DESede/ECB/NoPadding (168)
     * DESede/ECB/PKCS5Padding (168)
     * RSA/ECB/PKCS1Padding (1024, 2048)
     * RSA/ECB/OAEPWithSHA-1AndMGF1Padding (1024, 2048)
     * RSA/ECB/OAEPWithSHA-256AndMGF1Padding (1024, 2048)
     *
     * @throws GeneralSecurityException
     */
    public static Key createKey(String algorithm, byte[] originalKey, int size) throws GeneralSecurityException {
        SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
        rand.setSeed(originalKey);

        KeyGenerator kgen = KeyGenerator.getInstance(algorithm);
        kgen.init(size, rand);

        return kgen.generateKey();
    }


    public static Key encodeKeyRSA(byte[] originalKey) throws GeneralSecurityException {
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(new X509EncodedKeySpec(originalKey));
    }


    public static Key decodeKeyRSA(byte[] originalKey) throws GeneralSecurityException {
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePrivate(new PKCS8EncodedKeySpec(originalKey));
    }


    public static Cipher encoder(String algorithm, Key secureKey) throws GeneralSecurityException {
        return cipher(algorithm, Cipher.ENCRYPT_MODE, secureKey, null);
    }


    public static Cipher encoder(String algorithm, Key secureKey, IvParameterSpec initial) throws GeneralSecurityException {
        return cipher(algorithm, Cipher.ENCRYPT_MODE, secureKey, initial);
    }


    public static Cipher decoder(String algorithm, Key secureKey) throws GeneralSecurityException {
        return cipher(algorithm, Cipher.DECRYPT_MODE, secureKey, null);
    }


    public static Cipher decoder(String algorithm, Key secureKey, IvParameterSpec initial) throws GeneralSecurityException {
        return cipher(algorithm, Cipher.DECRYPT_MODE, secureKey, initial);
    }


    private static Cipher cipher(String algorithm, int mode, Key secureKey, IvParameterSpec initial) throws GeneralSecurityException {
        Cipher result = Cipher.getInstance(algorithm);
        result.init(mode, secureKey, initial);
        return result;
    }





    /**
     * Подсчитать контрольную сумму указанных ресурсов
     *
     * @param algorithm - алгоритм контрольной суммы
     * @param source    - набор источников
     * @return контрольная сумма
     */
    public static byte[] hash(String algorithm, byte[] ... source) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);

        for (byte[] part : source)
            digest.update(part);

        return digest.digest();
    }




    private Crypt() {}
}
