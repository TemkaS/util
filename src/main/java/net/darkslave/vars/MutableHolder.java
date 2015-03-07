/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.vars;



/**
 * Изменяемое значение
 */
public class MutableHolder<T> implements Holder<T> {
    private T value;


    public MutableHolder() {
    }


    public MutableHolder(T value) {
        this.value = value;
    }


    @Override
    public void set(T value) {
        this.value = value;
    }


    @Override
    public T get() {
        return value;
    }

}
