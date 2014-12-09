package serv.core;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;




public class ResponseOutputStream extends BufferedOutputStream {
    private boolean closed = false;


    public ResponseOutputStream(OutputStream out, int bufferSize) {
        super(out, bufferSize);
    }


    @Override
    public void close() throws IOException {
        super.close();
        closed = true;
    }


    public boolean isClosed() {
        return closed;
    }


}
