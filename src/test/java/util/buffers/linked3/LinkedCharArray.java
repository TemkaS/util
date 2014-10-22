package util.buffers.linked3;

import java.io.IOException;
import java.io.Writer;





/**
 * Динамический массив символов, построенный на связанных нодах
 */
public class LinkedCharArray {
    private Node head;
    private Node tail;
    private int  length;


    public LinkedCharArray() {
        head = tail = null;
        length = 0;
    }


    /**
     * Добавить блок символов
     *
     * @param source - источник
     * @param from - начало блока
     * @param size - размер блока
     */
    public void append(char[] source, int from, int size) {
        if (from < 0 || size < 0 || from + size > source.length)
            throw new IndexOutOfBoundsException();
        if (size > 0) {
            append(allocate(from, size).copy(source, from, size));
        }
    }


    /**
     * Добавить блок символов
     *
     * @param source - источник
     */
    public void append(char[] source) {
        int size = source.length;
        if (size > 0) {
            append(allocate(0, size).copy(source, 0, size));
        }
    }


    /**
     * Добавить блок символов
     *
     * @param source - источник
     */
    public void append(String source) {
        if (source == null)
            throw new NullPointerException();
        int size = source.length();
        if (size > 0) {
            append(allocate(0, size).copy(source));
        }
    }


    private void append(Node node) {
        if (tail == null) {
            head = tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        length+= node.size;
    }


    public void writeTo(Writer target) throws IOException {
        if (target == null)
            throw new NullPointerException();

        for (Node node = head; node != null; node = node.next) {
            target.write(node.target, node.from, node.size);
        }

    }


    public char[] toCharArray() {
        char[] result = new char[length];
        int index = 0;

        for (Node node = head; node != null; node = node.next) {
            System.arraycopy(node.target, node.from, result, index, node.size);
            index+= node.size;
        }

        return result;
    }


    public int length() {
        return length;
    }


    @Override
    public String toString() {
        return "[" + LinkedCharArray.class.getSimpleName() + ": " + length + " chars]";
    }


    private static int BUFFER_WHEN = 8;
    private static int BUFFER_SIZE = 256;

    private static char[] buffer = null;
    private static int    cursor = 0;


    private Node allocate(int from, int size) {
        if (size > BUFFER_WHEN)
            return new Node(new char[size], 0, size);

        if (buffer == null || cursor + size > buffer.length) {
            buffer = new char[BUFFER_SIZE];
            cursor = 0;
        }

        Node node = new Node(buffer, cursor, size);
        cursor+= size;

        return node;
    }


    private static class Node {
        /**
         * источник
         */
        public final char[] target;

        /**
         * начало блока
         */
        public final int from;

        /**
         * размер блока
         */
        public final int size;

        /**
         * следующий блок
         */
        public Node next;


        public Node(char[] target, int from, int size) {
            this.target = target;
            this.from   = from;
            this.size   = size;
        }


        public Node copy(char[] source, int from, int size) {
            System.arraycopy(source, from, this.target, this.from, size);
            return this;
        }

        public Node copy(String source) {
            source.getChars(0, source.length(), this.target, this.from);
            return this;
        }

    }

}
