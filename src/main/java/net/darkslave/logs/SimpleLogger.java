/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.logs;


import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;




/**
 * Примитивная реализация логгера ошибок с записью в поток
 */
public class SimpleLogger extends AbstractLogger implements Closeable {
    private static final String DEFAULT_FORMAT  = "yyyy-MM-dd HH:mm:ss.S";

    private volatile DateFormat format = new SimpleDateFormat(DEFAULT_FORMAT);
    private final PrintStream   stream;


    public SimpleLogger(PrintStream stream) {
        if (stream == null)
            throw new IllegalArgumentException("Stream can't be null");
        this.stream = stream;
    }


    public SimpleLogger(OutputStream stream, String charset) throws IOException {
        this(
            stream instanceof PrintStream
            ? (PrintStream) stream
            : new PrintStream(stream, true, charset)
        );
    }


    @SuppressWarnings("resource")
    public SimpleLogger(String filename, String charset, boolean append) throws IOException {
        this(new FileOutputStream(filename, append), charset);
    }


    public void setDateFormat(String format) {
        this.format = new SimpleDateFormat(format);
    }


    @Override
    public void close() throws IOException {
        stream.close();
    }


    @Override
    protected void print(String type, String content, Throwable e) {
        synchronized (stream) {
            DateFormat result = format;

            stream.print(result.format(new Date()));
            stream.print(" " + type + " ");
            stream.print(content);

            if (e != null)
                e.printStackTrace(stream);

            stream.println();
        }
    }

}
