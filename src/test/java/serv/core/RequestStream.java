package serv.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.sun.net.httpserver.HttpExchange;
import net.darkslave.io.Streams;





public class RequestStream  {
    private final Map<String, String> cache = new HashMap<String, String>(4);
    private final HttpExchange exchange;
    private byte[] bytes = null;


    public RequestStream(HttpExchange exchange) {
        this.exchange = exchange;
    }


    public byte[] getBytes() throws IOException {
        if (bytes == null)
            bytes = Streams.readAll(exchange.getRequestBody());
        return bytes;
    }


    public String getString(String charset) throws IOException {
        String result = cache.get(charset);

        if (result == null) {
            result = new String(getBytes(), charset);
            cache.put(charset, result);
        }

        return result;
    }


}
