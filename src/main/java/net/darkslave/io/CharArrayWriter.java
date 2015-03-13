package net.darkslave.io;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;



/**
 * Реализация java.io.CharArrayWriter без синхронизации
 */
public class CharArrayWriter extends Writer {
    private char[] target;
    private int cursor = 0;


    public CharArrayWriter() {
        this(128);
    }


    public CharArrayWriter(int defaultSize) {
        target = new char[defaultSize];
    }


    private char[] allocate(int requiredSize) {
        char[] temp = new char[ Math.max(requiredSize, target.length << 1) ];
        System.arraycopy(target, 0, temp, 0, cursor);
        return temp;
    }


    @Override
    public void write(int source) {
        int requiredSize = cursor + 1;

        if (target.length < requiredSize)
            target = allocate(requiredSize);

        target[cursor] = (char) source;

        cursor++;
    }


    @Override
    public void write(char[] source) {
        write(source, 0, source.length);
    }


    @Override
    public void write(char[] source, int from, int size) {
        int requiredSize = cursor + size;

        if (target.length < requiredSize)
            target = allocate(requiredSize);

        System.arraycopy(source, from, target, cursor, size);

        cursor+= size;
    }


    @Override
    public void write(String source) {
        write(source, 0, source.length());
    }


    @Override
    public void write(String source, int from, int size) {
        int requiredSize = cursor + size;

        if (target.length < requiredSize)
            target = allocate(requiredSize);

        source.getChars(from, from + size, target, cursor);

        cursor+= size;
    }


    @Override
    public Writer append(CharSequence source) {
        if (source != null) {
            String temp = source.toString();
            write(temp, 0, temp.length());
        } else {
            write("null");
        }
        return this;
    }


    @Override
    public Writer append(CharSequence source, int from, int till) {
        if (source != null) {
            String temp = source.subSequence(from, till).toString();
            write(temp, 0, temp.length());
        } else {
            write("null");
        }
        return this;
    }


    @Override
    public Writer append(char source) {
        write(source);
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
        return new String(target, 0, cursor);
    }


    public void writeTo(Writer output) throws IOException {
        output.write(target, 0, cursor);
    }


    public char[] toCharArray() {
        return Arrays.copyOf(target, cursor);
    }


}
