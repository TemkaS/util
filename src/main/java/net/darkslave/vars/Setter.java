/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.vars;



@FunctionalInterface
public interface Setter<T, V> {

    void set(T source, V value);

}
