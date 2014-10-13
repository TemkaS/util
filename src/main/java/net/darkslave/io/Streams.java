/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.io;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;




/**
 * Сервис чтения данных из потока
 */
public class Streams {
    private static final int BYTE_BUFFER_SIZE = 16 * 1024;


    /**
     * Чтение данных из потока в поток
     *
     * @param input  - поток-источник
     * @param output - целевой поток
     * @return количество скопированных байт
     * @throws IOException
     */
    public static long copy(InputStream input, OutputStream output) throws IOException {
        byte[] temp = new byte[BYTE_BUFFER_SIZE];

        long total = 0;

        while (true) {
            int read = input.read(temp);

            if (read < 0)
                break;

            output.write(temp, 0, read);
            total+= read;
        }

        return total;
    }


    /**
     * Чтение данных из потока в поток с указанием лимита
     *
     * @param input  - поток-источник
     * @param output - целевой поток
     * @param limit  - лимит чтения
     * @return количество скопированных байт
     * @throws IOException
     */
    public static long copy(InputStream input, OutputStream output, long limit) throws IOException {
        if (limit <= 0)
            return 0;

        byte[] temp = new byte[BYTE_BUFFER_SIZE];

        long total = 0;

        while (true) {
            int need = BYTE_BUFFER_SIZE;

            if (need > limit)
                need = (int) limit;

            int read = input.read(temp, 0, need);

            if (read < 0)
                break;

            output.write(temp, 0, read);
            total+= read;
            limit-= read;

            if (limit <= 0)
                break;
        }

        return total;
    }


    /**
     * Чтение данных из потока в массив байт
     */
    public static byte[] readAll(InputStream input) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();

        copy(input, result);

        return result.toByteArray();
    }


    /**
     * Чтение данных из потока в строку
     */
    public static String readAll(InputStream input, String charset) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();

        copy(input, result);

        return result.toString(charset);
    }



    private static final int CHAR_BUFFER_SIZE = 8 * 1024;


    /**
     * Чтение данных из потока в поток
     */
    public static long copy(Reader input, Writer output) throws IOException {
        char[] temp = new char[CHAR_BUFFER_SIZE];

        long total = 0;

        while (true) {
            int read = input.read(temp);

            if (read < 0)
                break;

            output.write(temp, 0, read);
            total+= read;
        }

        return total;
    }


    /**
     * Чтение данных из потока в поток
     */
    public static long copy(Reader input, Writer output, long limit) throws IOException {
        if (limit <= 0)
            return 0;

        char[] temp = new char[CHAR_BUFFER_SIZE];

        long total = 0;

        while (true) {
            int need = CHAR_BUFFER_SIZE;

            if (need > limit)
                need = (int) limit;

            int read = input.read(temp, 0, need);

            if (read < 0)
                break;

            output.write(temp, 0, read);
            total+= read;
            limit-= read;

            if (limit <= 0)
                break;
        }

        return total;
    }


    /**
     * Чтение данных из потока в строку
     */
    public static String readAll(Reader input) throws IOException {
        CharArrayWriter result = new CharArrayWriter();

        copy(input, result);

        return result.toString();
    }


    private Streams() {}
}
