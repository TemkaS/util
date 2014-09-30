/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
 package net.darkslave.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import net.darkslave.io.Streams;








/**
 * Загрузчик данных по http протоколу
 */
public class HttpLoader {
    private final HttpURLConnection connection;


    public HttpLoader(HttpURLConnection connection) {
        this.connection = connection;
    }


    public void setRequestMethod(String method) throws ProtocolException {
        connection.setRequestMethod(method);
    }


    public void setRequest(InputStream data) throws IOException {
        Streams.copy(data, connection.getOutputStream());
    }


    public void setRequest(byte[] data) throws IOException {
        connection.getOutputStream().write(data);
    }


    public void setConnectTimeout(int timeout) {
        connection.setConnectTimeout(timeout);
    }


    public void setReadTimeout(int timeout) {
        connection.setReadTimeout(timeout);
    }


    public void setRequestHeader(String key, String ... value) {
        int length = value.length;

        if (length == 0)
            throw new IllegalArgumentException("Header value can't be empty");

        connection.setRequestProperty(key, value[0]);

        if (length == 1)
            return;

        for (int i = 1; i < length; i++)
            connection.addRequestProperty(key, value[i]);

    }


    public int getResponseCode() throws IOException {
        return connection.getResponseCode();
    }


    private InputStream getStream() throws IOException {
        InputStream stream;

        stream = connection.getErrorStream();

        if (stream != null)
            return stream;

        stream = connection.getInputStream();

        return stream;
    }


    public byte[] getResponse() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Streams.copy(getStream(), stream);
        return stream.toByteArray();
    }




}
