package serv.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.darkslave.prop.PropertyFilePresenter;
import net.darkslave.prop.PropertyPresenter;
import serv.core.HandlerAdapter;
import com.sun.net.httpserver.HttpServer;



public class Main {
    private static final boolean DEBUG_MODE = false;
    private static final String   PROPERTY_FILE = DEBUG_MODE ? "src/test/server.properties" : "server.properties";


    public static void main(String[] args) throws IOException {
        PropertyPresenter properties = new PropertyFilePresenter(PROPERTY_FILE, StandardCharsets.UTF_8);
        PropertyPresenter serverProp = properties.getChild("server.");

        int serverPort = serverProp.getInteger("port", 8080);
        int threadsMin = serverProp.getInteger("threadsMin", 4);
        int threadsMax = serverProp.getInteger("threadsMax", 4);
        int requestQueue = serverProp.getInteger("requestQueue", 256);

        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);

        server.createContext("/", new HandlerAdapter());

        server.setExecutor(new ThreadPoolExecutor(threadsMin, threadsMax, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(requestQueue)));
        server.start();

        System.out.println("Server started at " + server.getAddress());
    }

}
