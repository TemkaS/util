/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.util;

import java.util.Iterator;
import java.util.NoSuchElementException;




/**
 * Итератор по элементам массива
 */
public class ArrayIterator<T> implements Iterator<T> {
    private final T[] array;
    private int index = 0;


    public ArrayIterator(T[] array) {
        if (array == null)
            throw new IllegalArgumentException("Source array can't be null");
        this.array = array;
    }


    @Override
    public boolean hasNext() {
        return index < array.length;
    }


    @Override
    public T next() {
        if (index >= array.length)
            throw new NoSuchElementException();
        return array[index++];
    }


    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}