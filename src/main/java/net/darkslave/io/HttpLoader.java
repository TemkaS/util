/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
 package net.darkslave.io;

import java.net.HttpURLConnection;
import java.net.ProtocolException;








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
