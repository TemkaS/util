package serv.impl;

import java.io.IOException;
import java.nio.file.Path;
import serv.core.Request;
import serv.core.RequestHandler;
import serv.core.Response;




public class FileHandler implements RequestHandler {
    private Path basePath;


    public FileHandler(Path basePath) {
        this.basePath = basePath;
    }


    @Override
    public void handle(Request request, Response response) throws IOException {
        String path = request.getRequestURI().getPath();

        try {
            Path file = basePath.resolve(path);

        } catch (Exception e) {

        }

    }

}
