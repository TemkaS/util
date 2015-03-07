/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.vars;




/**
 * Интерфейс хранилища значения
 */
public interface Holder<T> {


    public void set(T value);


    public T get();


}
