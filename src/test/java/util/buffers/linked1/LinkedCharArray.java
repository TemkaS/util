package util.buffers.linked1;

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
            append(new Node(source, from, size));
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
            append(new Node(source));
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
            append(new Node(source));
        }
    }


    private void append(Node node) {
        if (tail == null) {
            head = tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        length+= node.length;
    }


    public void writeTo(Writer target) throws IOException {
        if (target == null)
            throw new NullPointerException();

        for (Node node = head; node != null; node = node.next) {
            target.write(node.target, 0, node.length);
        }

    }


    public char[] toCharArray() {
        char[] result = new char[length];
        int index = 0;

        for (Node node = head; node != null; node = node.next) {
            System.arraycopy(node.target, 0, result, index, node.length);
            index+= node.length;
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


    private static class Node {
        /**
         * источник
         */
        public final char[] target;

        /**
         * размер блока
         */
        public final int length;

        /**
         * следующий блок
         */
        public Node next;


        public Node(char[] source) {
            length = source.length;
            target = new char[length];
            System.arraycopy(source,    0, target, 0, length);
        }


        public Node(char[] source, int from, int size) {
            length = size;
            target = new char[length];
            System.arraycopy(source, from, target, 0, length);
        }


        public Node(String source) {
            target = source.toCharArray();
            length = target.length;
        }

    }

}
