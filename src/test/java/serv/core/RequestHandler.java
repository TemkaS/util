package serv.core;

import java.io.IOException;





public interface RequestHandler {

    void handle(Request request, Response response) throws IOException;

}
