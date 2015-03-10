/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.vars;



@FunctionalInterface
public interface Getter<T, V> {

    V get(T source);

}
