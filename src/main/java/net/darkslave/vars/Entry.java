/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.vars;



/**
 * Класс key-value хранилища
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


    private volatile int hash = 0;

    @Override
    public int hashCode() {
        int temp = hash;

        if (temp == 0) {
            temp = 11;
            temp+= 31 * temp + (key != null ? key.hashCode() : 17);
            temp+= 31 * temp + (val != null ? val.hashCode() : 17);
            hash = temp;
        }

        return temp;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (obj == null || !(obj instanceof Entry))
            return false;

        Entry<?, ?> other = (Entry<?, ?>) obj;

        return ((key == other.key) || (key != null && key.equals(other.key)))
            && ((val == other.val) || (val != null && val.equals(other.val)));
    }

}
