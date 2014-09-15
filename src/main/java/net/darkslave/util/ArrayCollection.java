/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.util;

import java.util.Arrays;
import java.util.Collection;




/**
 * Коллекция-обертка над массивом
 */
public class ArrayCollection<T> extends ArrayIterable<T> implements Collection<T> {


    public ArrayCollection(T[] array) {
        super(array);
    }


    @Override
    public int size() {
        return array.length;
    }


    @Override
    public boolean isEmpty() {
        return array.length == 0;
    }


    @Override
    public boolean contains(Object value) {
        return Misc.inArray(value, array);
    }


    @Override
    public boolean containsAll(Collection<?> other) {
        for (Object value : other)
            if (!Misc.inArray(value, array))
                return false;
        return true;
    }


    @Override
    public Object[] toArray() {
        return Arrays.copyOf(array, array.length);
    }


    @Override
    @SuppressWarnings("unchecked")
    public <E> E[] toArray(E[] other) {
        int need = array.length,
            have = other.length;

        if (need > have)
            return (E[]) Arrays.copyOf(array, need, other.getClass());

        System.arraycopy(array, 0, other, 0, need);

        for (; need < have; need++)
            other[need] = null;

        return other;
    }


    @Override
    public boolean add(T e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

}