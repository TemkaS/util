package serv.core;


import java.net.InetSocketAddress;
import java.net.URI;
import serv.core.HttpData.ParameterException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;





public class Request {
    private final HttpExchange  exchange;
    private final RequestStream requestStream;


    public Request(HttpExchange exchange) {
        this.exchange = exchange;
        this.requestStream = new RequestStream(exchange);
    }


    public Object getAttribute(String name) {
        return exchange.getAttribute(name);
    }


    public void setAttribute(String name, Object value) {
        exchange.setAttribute(name, value);
    }


    public InetSocketAddress getLocalAddress() {
        return exchange.getLocalAddress();
    }


    public InetSocketAddress getRemoteAddress() {
        return exchange.getRemoteAddress();
    }


    public String getProtocol() {
        return exchange.getProtocol();
    }


    public Headers getRequestHeaders() {
        return exchange.getRequestHeaders();
    }


    public String getRequestMethod() {
        return exchange.getRequestMethod();
    }


    public URI getRequestURI() {
        return exchange.getRequestURI();
    }


    public RequestStream getRequestStream() {
        return requestStream;
    }


    public static void parseAdd(String source) throws ParameterException {
        int length = source.length();
        int index  = 0;

        while (index < length) {
            int close = source.indexOf('&', index);
            if (close < 0)
                close = length;

            String param = source.substring(index, close);


            index = close + 1;
        }
    }

}
