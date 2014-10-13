package net.darkslave.crypto;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;









public class Bytes {
    private static final String EMPTY_STRING = "";
    private static final byte[] EMPTY_BYTES = new byte[0];


    private static final String[] BIT_CHARS = new String[] {
        "0000", "0001", "0010", "0011",
        "0100", "0101", "0110", "0111",
        "1000", "1001", "1010", "1011",
        "1100", "1101", "1110", "1111",
    };


    private static final char[] HEX_CHARS = new char[] {
        '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };



    /**
     * Получить битовое представление набора байт
     */
    public static String printBit(byte[] source) {
        if (isEmpty(source))
            return EMPTY_STRING;

        int length = source.length;
        StringBuilder result = new StringBuilder(length << 3);

        for (int i = 0; i < length; i++) {
            byte b = source[i];
            result.append(BIT_CHARS[(b >>> 4) & 15]);
            result.append(BIT_CHARS[(b      ) & 15]);
        }

        return result.toString();
    }



    /**
     * Получить hex представление набора байт
     */
    public static String printHex(byte[] source) {
        if (isEmpty(source))
            return EMPTY_STRING;

        int length = source.length;
        StringBuilder result = new StringBuilder(length << 1);

        for (int i = 0; i < length; i++) {
            byte b = source[i];
            result.append(HEX_CHARS[(b >>> 4) & 15]);
            result.append(HEX_CHARS[(b      ) & 15]);
        }

        return result.toString();
    }


    /**
     * Получить набор байт по битовому представлению
     */
    public static byte[] fromBit(String source) {
        if (isEmpty(source))
            return EMPTY_BYTES;

        int length = source.length();

        if ((length & 7) != 0)
            throw new IllegalArgumentException("Source length " + length + " is not valid");

        byte[] result = new byte[length >>> 3];
        int i = 0;
        int j = 0;
        int c;

        while (i < length) {
            c = fromBitChar(source, i++) << 7;
            c|= fromBitChar(source, i++) << 6;
            c|= fromBitChar(source, i++) << 5;
            c|= fromBitChar(source, i++) << 4;
            c|= fromBitChar(source, i++) << 3;
            c|= fromBitChar(source, i++) << 2;
            c|= fromBitChar(source, i++) << 1;
            c|= fromBitChar(source, i++);
            result[j++] = (byte) c;
        }

        return result;
    }


    public static int fromBitChar(String source, int index) {
        char c = source.charAt(index);

        if (c == '0' || c == '1')
            return c - '0';

        throw new IllegalArgumentException("Illegal character `" + c + "` at position " + index);
    }



    /**
     * Получить набор байт по hex представлению
     */
    public static byte[] fromHex(String source) {
        if (isEmpty(source))
            return EMPTY_BYTES;

        int length = source.length();

        if ((length & 1) != 0)
            throw new IllegalArgumentException("Source length " + length + " is not valid");

        byte[] result = new byte[length >>> 1];
        int i = 0;
        int j = 0;
        int c;

        while (i < length) {
            c = fromHexChar(source, i++) << 4;
            c|= fromHexChar(source, i++);
            result[j++] = (byte) c;
        }

        return result;
    }


    public static int fromHexChar(String source, int index) {
        char c = source.charAt(index);

        if (c >= '0' && c <= '9')
            return c - '0';

        if (c >= 'A' && c <= 'F')
            return c - 'A' + 10;

        if (c >= 'a' && c <= 'f')
            return c - 'a' + 10;

        throw new IllegalArgumentException("Illegal character `" + c + "` at position " + index);
    }


    /**
     * Преобразование строки в набор байт
     */
    public static byte[] from(String source, Charset charset) throws UnsupportedEncodingException {
        return source != null ? source.getBytes(charset) : EMPTY_BYTES;
    }

    /**
     * Преобразование Boolean в набор байт
     */
    public static byte[] from(Boolean source) {
        return source != null ? from(source.booleanValue()) : EMPTY_BYTES;
    }

    /**
     * Преобразование Byte в набор байт
     */
    public static byte[] from(Byte source) {
        return source != null ? from(source.byteValue()) : EMPTY_BYTES;
    }

    /**
     * Преобразование Short в набор байт
     */
    public static byte[] from(Short source) {
        return source != null ? from(source.shortValue()) : EMPTY_BYTES;
    }

    /**
     * Преобразование Character в набор байт
     */
    public static byte[] from(Character source) {
        return source != null ? from(source.charValue()) : EMPTY_BYTES;
    }

    /**
     * Преобразование Integer в набор байт
     */
    public static byte[] from(Integer source) {
        return source != null ? from(source.intValue()) : EMPTY_BYTES;
    }

    /**
     * Преобразование Long в набор байт
     */
    public static byte[] from(Long source) {
        return source != null ? from(source.longValue()) : EMPTY_BYTES;
    }

    /**
     * Преобразование Float в набор байт
     */
    public static byte[] from(Float source) {
        return source != null ? from(source.floatValue()) : EMPTY_BYTES;
    }

    /**
     * Преобразование Double в набор байт
     */
    public static byte[] from(Double source) {
        return source != null ? from(source.doubleValue()) : EMPTY_BYTES;
    }

    /**
     * Преобразование boolean в набор байт
     */
    public static byte[] from(boolean source) {
        return new byte[] {
                source ? (byte) 1 : (byte) 0
        };
    }

    /**
     * Преобразование byte в набор байт
     */
    public static byte[] from(byte source) {
        return new byte[] {
                source
        };
    }

    /**
     * Преобразование short в набор байт
     */
    public static byte[] from(short source) {
        return new byte[] {
                (byte) (source >> 8),
                (byte) (source)
        };
    }

    /**
     * Преобразование char в набор байт
     */
    public static byte[] from(char source) {
        return new byte[] {
                (byte) (source >> 8),
                (byte) (source)
        };
    }

    /**
     * Преобразование int в набор байт
     */
    public static byte[] from(int source) {
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
    public static byte[] from(long source) {
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
    public static byte[] from(float source) {
        return from(Float.floatToIntBits(source));
    }

    /**
     * Преобразование double в набор байт
     */
    public static byte[] from(double source) {
        return from(Double.doubleToLongBits(source));
    }



    private static boolean isEmpty(byte[] source) {
        return source == null || source.length == 0;
    }


    private static boolean isEmpty(CharSequence source) {
        return source == null || source.length() == 0;
    }


    private Bytes() {}
}
