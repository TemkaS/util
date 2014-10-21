package util.buffers.linked2;

import java.io.IOException;
import java.io.Writer;





/**
 * Динамический массив символов, построенный на связанных нодах
 */
public class LinkedCharArray {
    private static final int BLOCK_SIZE = 4096;

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
     * @param data - источник
     * @param from - начало блока
     * @param size - размер блока
     */
    public void append(char[] data, int from, int size) {
        if (from < 0 || size < 0 || from + size > data.length)
            throw new IndexOutOfBoundsException();
        if (size > 0) {
            __append(data, from, size);
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
            __append(data, 0, size);
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
            char[] temp = data.toCharArray();
            __append(temp, 0, temp.length);
        }
    }


    private void __append(char[] data, int from, int size) {
        if (tail == null) {
            head = tail = new Node(data, from, size);
        } else {
            tail = tail.append(data, from, size);
        }
        length+= size;
    }




    public void writeTo(Writer target) throws IOException {
        if (target == null)
            throw new NullPointerException();

        for (Node node = head; node != null; node = node.next) {
            target.write(node.target, 0, node.count);
        }

    }


    public char[] toCharArray() {
        char[] result = new char[length];
        int    count  = 0;

        for (Node node = head; node != null; node = node.next) {
            System.arraycopy(node.target, 0, result, count, node.count);
            count+= node.count;
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
         * блок символов
         */
        public final char[] target;

        /**
         * размер блока
         */
        public final int length;

        /**
         * текущая позиция в блоке
         */
        public int count;

        /**
         * следующая нода
         */
        public Node next;


        public Node(char[] data, int from, int size) {
            length = size < BLOCK_SIZE ? BLOCK_SIZE : size;
            target = new char[length];

            System.arraycopy(data, from, target, 0, size);
            count  = size;
        }


        public Node append(char[] data, int from, int size) {
            int free = length - count;

            if (free >= size) {
                System.arraycopy(data, from, target, count, size);
                count+= size;
                return this;
            }

            if (free > 0) {
                System.arraycopy(data, from, target, count, free);
                count+= free;
                from += free;
                size -= free;
            }

            return next = new Node(data, from, size);
        }


    }

}
