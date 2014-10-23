package net.darkslave.io;

import java.io.IOException;
import java.io.Writer;




public class StringWriter extends Writer {
    private final StringBuilder target;


    public StringWriter(int defaultSize) {
        target = new StringBuilder(defaultSize);
    }


    @Override
    public void write(int source) throws IOException {
        target.append((char) source);
    }


    @Override
    public void write(char[] source) throws IOException {
        target.append(source);
    }


    @Override
    public void write(char[] source, int from, int size) throws IOException {
        target.append(source, from, size);
    }


    @Override
    public void write(String source) throws IOException {
        target.append(source);
    }


    @Override
    public void write(String source, int from, int size) throws IOException {
        target.append(source, from, from + size);
    }


    @Override
    public Writer append(CharSequence source) throws IOException {
        target.append(source);
        return this;
    }


    @Override
    public Writer append(CharSequence source, int from, int till) throws IOException {
        target.append(source, from, till);
        return this;
    }


    @Override
    public Writer append(char source) throws IOException {
        target.append(source);
        return this;
    }


    @Override
    public void flush() throws IOException {
    }


    @Override
    public void close() throws IOException {
    }


    @Override
    public String toString() {
        return target.toString();
    }


}
