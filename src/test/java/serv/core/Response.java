package serv.core;


import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;





public class Response {
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static final int BUFFER_SIZE = 16 * 1024;

    private final HttpExchange exchange;
    private final ResponseOutputStream output;


    public Response(HttpExchange exchange) {
        this.exchange = exchange;
        this.output   = new ResponseOutputStream(exchange.getResponseBody(), BUFFER_SIZE);
    }


    public Object getAttribute(String name) {
        return exchange.getAttribute(name);
    }


    public void setAttribute(String name, Object value) {
        exchange.setAttribute(name, value);
    }


    public OutputStream getOutputStream() {
        return output;
    }


    public int getResponseCode() {
        return exchange.getResponseCode();
    }


    public Headers getResponseHeaders() {
        return exchange.getResponseHeaders();
    }


    public void sendResponseHeaders(int statusCode, long contentLength) throws IOException {
        exchange.sendResponseHeaders(statusCode, contentLength);
    }


    public boolean isClosed() {
        return output.isClosed();
    }


    public void sendError(int statusCode, String content) {

    }

}
