/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.crypto.Cipher;





public class RSACipher {
    private static int KEY_PADDING = 11;

    private final Lock encodeLock = new ReentrantLock();
    private final Lock decodeLock = new ReentrantLock();

    private final PublicKey  publicKey;
    private final PrivateKey privateKey;

    private volatile Cipher encoder;
    private volatile Cipher decoder;


    /**
     * Создать новый шифровщик с ключами указанной длины
     */
    public static RSACipher createNewCipher(int size) throws GeneralSecurityException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(size);

        KeyPair pair = generator.generateKeyPair();

        return new RSACipher(pair.getPublic(), pair.getPrivate());
    }


    /**
     * Создать шифровщик с указанными ключами
     */
    public static RSACipher createCipher(PublicKey publicKey, PrivateKey privateKey) {
        return new RSACipher(publicKey, privateKey);
    }


    /**
     * Создать шифровщик с указанными ключами
     */
    public static RSACipher createCipher(byte[] publicKey, byte[] privateKey) throws GeneralSecurityException {
        return new RSACipher(publicKey, privateKey);
    }


    private RSACipher(PublicKey publicKey, PrivateKey privateKey) {
        this.publicKey  = publicKey;
        this.privateKey = privateKey;
    }


    private RSACipher(byte[] publicKey, byte[] privateKey) throws GeneralSecurityException {
        KeyFactory factory = KeyFactory.getInstance("RSA");

        if (!isEmpty(publicKey)) {
            this.publicKey  = factory.generatePublic(new X509EncodedKeySpec(publicKey));
        } else {
            this.publicKey  = null;
        }

        if (!isEmpty(privateKey)) {
            this.privateKey = factory.generatePrivate(new PKCS8EncodedKeySpec(privateKey));
        } else {
            this.privateKey = null;
        }
    }


    public PublicKey getPublicKey() {
        return publicKey;
    }


    public PrivateKey getPrivateKey() {
        return privateKey;
    }


    /**
     * Закодировать сообщение
     */
    public void encode(InputStream inp, OutputStream out) throws IOException, GeneralSecurityException {
        encodeLock.lock();

        try {
            Cipher cipher = encoder();

            int size = cipher.getOutputSize(0),
                part = size - KEY_PADDING,
                read;

            if (part <= 0)
                throw new GeneralSecurityException("Public key is not correct");

            byte[] temp = new byte[part];

            while ((read = inp.read(temp)) > 0) {
                out.write(cipher.doFinal(temp, 0, read));
            }

        } finally {
            encodeLock.unlock();
        }
    }


    /**
     * Декодировать сообщение
     */
    public void decode(InputStream inp, OutputStream out) throws IOException, GeneralSecurityException {
        decodeLock.lock();

        try {
            Cipher cipher = decoder();

            int part = cipher.getOutputSize(0),
                read;

            byte[] temp = new byte[part];

            while ((read = inp.read(temp)) > 0) {
                out.write(cipher.doFinal(temp, 0, read));
            }

        } finally {
            decodeLock.unlock();
        }
    }


    private Cipher encoder() throws GeneralSecurityException {
        if (encoder != null)
            return encoder;

        if (publicKey == null)
            throw new GeneralSecurityException("Public key is not defined");

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        encoder = cipher;

        return encoder;
    }


    private Cipher decoder() throws GeneralSecurityException {
        if (decoder != null)
            return decoder;

        if (privateKey == null)
            throw new GeneralSecurityException("Private key is not defined");

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        decoder = cipher;

        return decoder;
    }


    private static boolean isEmpty(byte[] source) {
        return source == null || source.length == 0;
    }


}
