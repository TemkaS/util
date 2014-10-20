package util;

import java.io.IOException;
import java.io.Writer;






public class LinkedCharBuffer {
    private static final String EMPTY_STRING = "";

    private Node head;
    private Node tail;

    private String cached;
    private int length;


    public LinkedCharBuffer() {
        head = tail = null;
        cached = null;
        length = 0;
    }


    /**
     * Добавить блок символов
     *
     * @param data - источник
     * @param from - начало блока
     * @param size - размер блока
     */
    public void append(char[] data, int from, int size) {
        if (from < 0 || size < 0 || from + size > data.length)
            throw new IndexOutOfBoundsException();
        if (size > 0) {
            append(new Node(data, from, size));
        }
    }


    /**
     * Добавить блок символов
     *
     * @param data - источник
     */
    public void append(char[] data) {
        int size = data.length;
        if (size > 0) {
            append(new Node(data, 0, size));
        }
    }


    /**
     * Добавить блок символов
     *
     * @param data - источник
     */
    public void append(String data) {
        if (data == null)
            throw new NullPointerException();
        int size = data.length();
        if (size > 0) {
            char[] temp = new char[size];
            data.getChars(0, size, temp, 0);
            append(new Node(temp, 0, size));
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
        cached = null;
    }


    public void writeTo(Writer target) throws IOException {
        if (target == null)
            throw new NullPointerException();

        for (Node node = head; node != null; node = node.next)
            target.write(node.data, node.from, node.size);

    }


    private String toString0() {
        if (tail == null)
            return EMPTY_STRING;

        if (tail == head)
            return new String(head.data, head.from, head.size);

        char[] data = new char[length];
        int from = 0;

        for (Node node = head; node != null; ) {
            Node temp = node;
            node = temp.next;
            temp.next = null;

            System.arraycopy(temp.data, temp.from, data, from, temp.size);
            from+= temp.size;

        }

        head = tail = new Node(data, 0, data.length);
        return new String(data);
    }


    @Override
    public String toString() {
        if (cached == null)
            return cached = toString0();
        return cached;
    }


    private static class Node {
        /**
         * источник
         */
        public final char[] data;

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


        public Node(char[] data, int from, int size) {
            this.data = data;
            this.from = from;
            this.size = size;
        }

    }

}
