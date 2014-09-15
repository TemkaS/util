/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
 package net.darkslave.io;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.security.GeneralSecurityException;
import net.darkslave.crypto.SSLFactory;








/**
 * Загрузчик данных по http протоколу
 */
public class HttpLoader {
    private final HttpURLConnection connection;


    public static HttpLoader create(SSLFactory factory, String path) throws IOException, GeneralSecurityException {
        return create(factory.createConnection(path));
    }


    public static HttpLoader create(String path) throws IOException, GeneralSecurityException {
        return create(SSLFactory.DEFAULT_FACTORY.createConnection(path));
    }


    public static HttpLoader create(HttpURLConnection connection) {
        return new HttpLoader(connection);
    }


    private HttpLoader(HttpURLConnection connection) {
        this.connection = connection;
    }


    public void setRequestMethod(String method) throws ProtocolException {
        connection.setRequestMethod(method);
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





}
