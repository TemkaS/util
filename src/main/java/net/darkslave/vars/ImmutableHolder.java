/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.vars;



/**
 * Неизменное значение
 */
public class ImmutableHolder<T> implements Holder<T> {
    private final T value;


    public ImmutableHolder(T value) {
        this.value = value;
    }


    @Override
    public void set(T value) {
        throw new UnsupportedOperationException();
    }


    @Override
    public T get() {
        return value;
    }

}
