/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.http;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
 *
 * thread safe
 */
public class SSLFactory {
    private final String           proto;
    private final KeyManager[]     keys;
    private final TrustManager[]   trust;
    private final HostnameVerifier hosts;
    private volatile SSLContext context;


    /**
     * Конструктор фабрики SSL сокетов
     *
     * @param proto - протокол SSL
     * @param keys  - менеджер ключей
     * @param trust - менеджер доверенных хостов
     * @param hosts - менеджер проверки хостов
     */
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
     * Создать http(s) соединение
     */
    public HttpURLConnection createConnection(URL u) throws IOException, GeneralSecurityException {
        URLConnection temp = u.openConnection();

        if (!(temp instanceof HttpURLConnection))
            throw new MalformedURLException("Protocol `" + u.getProtocol() + "` is not supported");

        HttpURLConnection http = (HttpURLConnection) temp;

        if (http instanceof HttpsURLConnection) {
            HttpsURLConnection https = (HttpsURLConnection) http;
            https.setSSLSocketFactory(getSocketFactory());
            https.setHostnameVerifier(hosts);
        }

        return http;
    }


    /**
     * Создать http(s) соединение
     */
    public HttpURLConnection createConnection(String path) throws IOException, GeneralSecurityException {
        return createConnection(new URL(path));
    }



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


    /**
     * Менеджер доверенных хостов по умолчанию
     */
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


    /**
     * Менеджер проверки хостов по умолчанию
     */
    public static final HostnameVerifier DEFAULT_HOST_VERIFIER = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };


    /**
     * Фабрика сокетов по умолчанию
     */
    public static final SSLFactory DEFAULT_FACTORY = new SSLFactory("SSL", null, DEFAULT_TRUST_MANAGER, DEFAULT_HOST_VERIFIER);


}
