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
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;




/**
 * Класс-шифровальщик по алгоритму RSA
 */
public class RSACipher extends SimpleCipher {

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
    public static RSACipher createCipher(PublicKey encodeKey, PrivateKey decodeKey) {
        return new RSACipher(encodeKey, decodeKey);
    }


    /**
     * Создать шифровщик с указанными ключами
     */
    public static RSACipher createCipher(byte[] encodeKeyRaw, byte[] decodeKeyRaw) throws GeneralSecurityException {
        KeyFactory factory = KeyFactory.getInstance("RSA");

        Key encodeKey = null;
        if (!isEmpty(encodeKeyRaw))
            encodeKey = factory.generatePublic(new X509EncodedKeySpec(encodeKeyRaw));

        Key decodeKey = null;
        if (!isEmpty(decodeKeyRaw))
            decodeKey = factory.generatePrivate(new PKCS8EncodedKeySpec(decodeKeyRaw));

        return new RSACipher(encodeKey, decodeKey);
    }


    private static boolean isEmpty(byte[] source) {
        return source == null || source.length == 0;
    }


    private RSACipher(Key encodeKey, Key decodeKey) {
        super("RSA", encodeKey, decodeKey);
    }


    @Override
    protected int encodePadding() {
        return 11;
    }

}
