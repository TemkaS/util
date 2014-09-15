/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.util;



/**
 * Класс-обертка
 */
public class Holder<T> {
    private final T value;


    public Holder(T value) {
        this.value = value;
    }


    public T value() {
        return value;
    }

}
