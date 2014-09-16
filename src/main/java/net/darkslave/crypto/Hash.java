/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.crypto;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;





public class Hash {
    private static final String EMPTY_STRING = "";

    private static final char[] HEX_CHARS = new char[] {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };



    /**
     * Подсчитать контрольную сумму указанных ресурсов
     *
     * @param algorithm - алгоритм контрольной суммы
     * @param source    - набор источников
     * @return контрольная сумма
     */
    public static byte[] getHash(String algorithm, byte[] ... source) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);

        for (byte[] part : source)
            digest.update(part);

        return digest.digest();
    }


    /**
     * Получить hex представление набора байт
     */
    public static String toHexString(byte[] source) {
        if (source == null || source.length == 0)
            return EMPTY_STRING;

        int length = source.length;
        char[] result = new char[2 * length];

        for (int i = 0, j = 0; i < length; i++, j += 2) {
            byte b = source[i];
            result[j    ] = HEX_CHARS[(b >> 4) & 15];
            result[j + 1] = HEX_CHARS[(b     ) & 15];
        }

        return new String(result);
    }


    /**
     * Получить набор байт по hex представлению
     */
    public static byte[] fromHexString(String source) {
        if (source == null || source.length() == 0)
            return new byte[0];

        if ((source.length() & 1) != 0)
            throw new IllegalArgumentException("Source length is not valid");

        int length = source.length() >> 1;
        byte[] result = new byte[length];

        for (int i = 0, j = 0; i < length; i++, j+= 2) {
            result[i] = (byte) (
                        (fromHexChar(source.charAt(j    )) << 4)
                      | (fromHexChar(source.charAt(j + 1))     )
                        );
        }

        return result;
    }


    private static int fromHexChar(char c) {
        if (c >= '0' && c <= '9')
            return c - '0';

        if (c >= 'A' && c <= 'F')
            return c - 'A' + 10;

        if (c >= 'a' && c <= 'f')
            return c - 'a' + 10;

        throw new IllegalArgumentException("Character `" + c + "` is not valid");
    }


    /**
     * Преобразование строки в набор байт
     */
    public static byte[] toBytes(String source, String charset) throws UnsupportedEncodingException {
        return source != null ? source.getBytes(charset) : new byte[0];
    }

    /**
     * Преобразование Boolean в набор байт
     */
    public static byte[] toBytes(Boolean source) {
        return source != null ? toBytes(source.booleanValue()) : new byte[0];
    }

    /**
     * Преобразование Byte в набор байт
     */
    public static byte[] toBytes(Byte source) {
        return source != null ? toBytes(source.byteValue()) : new byte[0];
    }

    /**
     * Преобразование Short в набор байт
     */
    public static byte[] toBytes(Short source) {
        return source != null ? toBytes(source.shortValue()) : new byte[0];
    }

    /**
     * Преобразование Character в набор байт
     */
    public static byte[] toBytes(Character source) {
        return source != null ? toBytes(source.charValue()) : new byte[0];
    }

    /**
     * Преобразование Integer в набор байт
     */
    public static byte[] toBytes(Integer source) {
        return source != null ? toBytes(source.intValue()) : new byte[0];
    }

    /**
     * Преобразование Long в набор байт
     */
    public static byte[] toBytes(Long source) {
        return source != null ? toBytes(source.longValue()) : new byte[0];
    }

    /**
     * Преобразование Float в набор байт
     */
    public static byte[] toBytes(Float source) {
        return source != null ? toBytes(source.floatValue()) : new byte[0];
    }

    /**
     * Преобразование Double в набор байт
     */
    public static byte[] toBytes(Double source) {
        return source != null ? toBytes(source.doubleValue()) : new byte[0];
    }

    /**
     * Преобразование boolean в набор байт
     */
    public static byte[] toBytes(boolean source) {
        return new byte[] {
                source ? (byte) 1 : (byte) 0
        };
    }

    /**
     * Преобразование byte в набор байт
     */
    public static byte[] toBytes(byte source) {
        return new byte[] {
                source
        };
    }

    /**
     * Преобразование short в набор байт
     */
    public static byte[] toBytes(short source) {
        return new byte[] {
                (byte) (source >> 8),
                (byte) (source)
        };
    }

    /**
     * Преобразование char в набор байт
     */
    public static byte[] toBytes(char source) {
        return new byte[] {
                (byte) (source >> 8),
                (byte) (source)
        };
    }

    /**
     * Преобразование int в набор байт
     */
    public static byte[] toBytes(int source) {
        return new byte[] {
                (byte) (source >> 24),
                (byte) (source >> 16),
                (byte) (source >> 8),
                (byte) (source)
        };
    }

    /**
     * Преобразование long в набор байт
     */
    public static byte[] toBytes(long source) {
        return new byte[] {
                (byte) (source >> 56),
                (byte) (source >> 48),
                (byte) (source >> 40),
                (byte) (source >> 32),
                (byte) (source >> 24),
                (byte) (source >> 16),
                (byte) (source >> 8),
                (byte) (source)
        };
    }

    /**
     * Преобразование float в набор байт
     */
    public static byte[] toBytes(float source) {
        return toBytes(Float.floatToIntBits(source));
    }

    /**
     * Преобразование double в набор байт
     */
    public static byte[] toBytes(double source) {
        return toBytes(Double.doubleToLongBits(source));
    }



    private Hash() {}
}
