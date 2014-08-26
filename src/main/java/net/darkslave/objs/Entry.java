/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.objs;



/**
 * Класс key-value хранилища
 *
 * @param <K>
 * @param <V>
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
    public String toString() {
        return "{" + key + ": " + val + "}";
    }


    private volatile int hash = 0;

    @Override
    public int hashCode() {
        int result = hash;

        if (result == 0) {
            result = 11;
            result+= 31 * result + (key != null ? key.hashCode() : 17);
            result+= 31 * result + (val != null ? val.hashCode() : 17);
            hash   = result;
        }

        return result;
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
