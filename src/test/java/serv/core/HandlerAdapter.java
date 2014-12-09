package serv.core;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;




public class HandlerAdapter implements HttpHandler {
    private final List<RequestHandler> handlers = new LinkedList<RequestHandler>();


    public boolean add(RequestHandler e) {
        return handlers.add(e);
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Request  request  = new Request (exchange);
        Response response = new Response(exchange);

        for (RequestHandler handler : handlers) {
            handler.handle(request, response);
            if (response.isClosed())
                return;
        }

        response.sendError(404, "Requested path `" + exchange.getRequestURI() + "` is not found");
    }


}
