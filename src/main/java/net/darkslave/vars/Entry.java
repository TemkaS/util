/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.vars;



/**
 * Класс key-value хранилища<br>
 * immutable только если key и val являются immutable
 */
public final class Entry<K, V> {
    private final K key;
    private final V val;


    public Entry(K key, V val) {
        this.key = key;
        this.val = val;
    }


    public K getKey() {
        return key;
    }


    public V getValue() {
        return val;
    }


    @Override
    public int hashCode() {
        int hash = 11;

        hash+= 31 * hash + (key != null ? key.hashCode() : 17);
        hash+= 31 * hash + (val != null ? val.hashCode() : 17);

        return hash;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (obj == null || !(obj instanceof Entry))
            return false;

        Entry<?, ?> oth = (Entry<?, ?>) obj;

        if (!(key == oth.key || (key != null && key.equals(oth.key))))
            return false;

        if (!(val == oth.val || (val != null && val.equals(oth.val))))
            return false;

        return true;
    }


}
