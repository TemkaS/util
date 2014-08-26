/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.crypto;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;



/**
 * Фабрика SSL сокетов
 */
public class SSLFactory {
    private final String           proto;
    private final KeyManager[]     keys;
    private final TrustManager[]   trust;
    private final HostnameVerifier hosts;
    private volatile SSLContext context;


    public SSLFactory(String proto, KeyManager[] keys, TrustManager[] trust, HostnameVerifier hosts) {
        this.proto = proto;
        this.keys  = keys;
        this.trust = trust;
        this.hosts = hosts;
    }


    private SSLContext getContext() throws GeneralSecurityException {
        if (context == null) {
            synchronized (this) {
                if (context == null) {
                    final SSLContext result = SSLContext.getInstance(proto);
                    result.init(keys, trust, new SecureRandom());
                    context = result;
                }
            }
        }
        return context;
    }


    /**
     * Получить фабрику сокетов
     */
    public SSLSocketFactory getSocketFactory() throws GeneralSecurityException {
        return getContext().getSocketFactory();
    }


    /**
     * Создать SSL сокет
     */
    public SSLSocket createSocket(String host, int port) throws IOException, GeneralSecurityException {
        return (SSLSocket) getSocketFactory().createSocket(host, port);
    }


    /**
     * Создать SSL сокет
     */
    public SSLSocket createSocket(InetAddress host, int port) throws IOException, GeneralSecurityException {
        return (SSLSocket) getSocketFactory().createSocket(host, port);
    }


    /**
     * Создать HTTPS соединение
     */
    public HttpURLConnection createConnection(URL url) throws IOException, GeneralSecurityException {
        HttpURLConnection result = (HttpURLConnection) url.openConnection();

        if (result instanceof HttpsURLConnection) {
            HttpsURLConnection https = (HttpsURLConnection) result;
            https.setSSLSocketFactory(getSocketFactory());
            https.setHostnameVerifier(hosts);
        }

        return result;
    }


    public static final TrustManager[] DEFAULT_TRUST_MANAGER = new TrustManager[] {
        new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
    };


    public static final HostnameVerifier DEFAULT_HOST_VERIFIER = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };


    /**
     * Прочитать ключи из указанного хранилища
     */
    public static KeyStore getKeyStore(String type, String path, String password) throws IOException, GeneralSecurityException {
        try (InputStream stream = new FileInputStream(path)) {
            KeyStore result = KeyStore.getInstance(type);
            result.load(stream, password.toCharArray());
            return result;
        }
    }


    /**
     * Инициализировать менеджера ключей
     */
    public static KeyManager[] getKeyManager(String type, KeyStore storage, String password) throws GeneralSecurityException {
        KeyManagerFactory factory = KeyManagerFactory.getInstance(type);
        factory.init(storage, password.toCharArray());
        return factory.getKeyManagers();
    }

}
