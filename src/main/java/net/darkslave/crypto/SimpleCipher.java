/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import javax.crypto.Cipher;




/**
 * Абстрактный класс шифровщика
 */
abstract public class SimpleCipher {
    private final String algorithm;
    private final Key encodeKey;
    private final Key decodeKey;

    private Cipher encoder;
    private Cipher decoder;


    /**
     * Алгоритм шифрования
     */
    public String getAlgorithm() {
        return algorithm;
    }


    /**
     * Ключ шифрования
     */
    public Key getEncodeKey() {
        return encodeKey;
    }


    /**
     * Ключ дешифрования
     */
    public Key getDecodeKey() {
        return decodeKey;
    }


    protected SimpleCipher(String algorithm, Key encodeKey, Key decodeKey) {
        this.algorithm = algorithm;
        this.encodeKey = encodeKey;
        this.decodeKey = decodeKey;
    }



    /**
     * Зашифровать поток сообщения
     *
     * @param source - поток-источник
     * @param result - целевой поток
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public void encode(InputStream source, OutputStream result) throws IOException, GeneralSecurityException {
        Cipher cipher = encoder();

        int size = cipher.getOutputSize(0),
            part = size - encodePadding(),
            read;

        if (part <= 0)
            throw new GeneralSecurityException("Encode key is not correct");

        byte[] temp = new byte[part];

        while ((read = source.read(temp)) > 0) {
            result.write(cipher.doFinal(temp, 0, read));
        }

    }


    /**
     * Зашифровать строку байт
     *
     * @param source - исходное сообщение
     * @return зашифрованное сообщение
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public byte[] encode(byte[] source) throws IOException, GeneralSecurityException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();

        encode(new ByteArrayInputStream(source), result);

        return result.toByteArray();
    }


    /**
     * Дешифровать поток сообщения
     *
     * @param source - поток-источник
     * @param result - целевой поток
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public void decode(InputStream source, OutputStream result) throws IOException, GeneralSecurityException {
        Cipher cipher = decoder();

        int part = cipher.getOutputSize(0),
            read;

        byte[] temp = new byte[part];

        while ((read = source.read(temp)) > 0) {
            result.write(cipher.doFinal(temp, 0, read));
        }

    }

    /**
     * Дешифровать строку байт
     *
     * @param source - исходное сообщение
     * @return дешифрованное сообщение
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public byte[] decode(byte[] source) throws IOException, GeneralSecurityException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();

        decode(new ByteArrayInputStream(source), result);

        return result.toByteArray();
    }



    protected Cipher encoder() throws GeneralSecurityException {
        if (encoder != null)
            return encoder;

        if (encodeKey == null)
            throw new GeneralSecurityException("Encode key is not defined");

        final Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, encodeKey);

        return encoder = cipher;
    }


    protected Cipher decoder() throws GeneralSecurityException {
        if (decoder != null)
            return decoder;

        if (decodeKey == null)
            throw new GeneralSecurityException("Decode key is not defined");

        final Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, decodeKey);

        return decoder = cipher;
    }


    abstract protected int encodePadding();

}
