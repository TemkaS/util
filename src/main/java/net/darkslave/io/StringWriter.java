/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.io;

import java.io.IOException;
import java.io.Writer;




/**
 * Реализация java.io.StringWriter без синхронизации
 */
public class StringWriter extends Writer {
    private final StringBuilder target;


    public StringWriter() {
        this(128);
    }


    public StringWriter(int defaultSize) {
        target = new StringBuilder(defaultSize);
    }


    @Override
    public void write(int source) {
        target.append((char) source);
    }


    @Override
    public void write(char[] source) {
        target.append(source);
    }


    @Override
    public void write(char[] source, int from, int size) {
        target.append(source, from, size);
    }


    @Override
    public void write(String source) {
        target.append(source);
    }


    @Override
    public void write(String source, int from, int size) {
        target.append(source, from, from + size);
    }


    @Override
    public Writer append(CharSequence source) {
        target.append(source);
        return this;
    }


    @Override
    public Writer append(CharSequence source, int from, int till) {
        target.append(source, from, till);
        return this;
    }


    @Override
    public Writer append(char source) {
        target.append(source);
        return this;
    }


    @Override
    public void flush() {
    }


    @Override
    public void close() {
    }


    @Override
    public String toString() {
        return target.toString();
    }


    public void writeTo(Writer output) throws IOException {
        output.append(target);
    }


}
