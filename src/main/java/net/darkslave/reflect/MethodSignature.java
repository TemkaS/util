/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.reflect;


import java.lang.reflect.Method;
import java.util.Arrays;





/**
 * Класс сигнатуры метода<br>
 * immutable
 */
public class MethodSignature {
    private static final Class<?>[] EMPTY_ARGS = new Class<?>[0];

    private final String name;
    private final Class<?>[] args;


    public MethodSignature(Method method) {
        if (method == null)
            throw new IllegalArgumentException("Parameter can't be null");

        this.name = method.getName();
        this.args = method.getParameterTypes();
    }


    public MethodSignature(String name, Class<?> ... args) {
        if (name == null)
            throw new IllegalArgumentException("Method name can't be null");

        if (args == null)
            throw new IllegalArgumentException("Method args can't be null");

        this.name = name;
        this.args = args.length > 0 ? args.clone() : EMPTY_ARGS;
    }


    public String getName() {
        return name;
    }


    public Class<?>[] getArgs() {
        return args;
    }


    private volatile int hash = 0;


    @Override
    public int hashCode() {
        int temp = hash;

        if (temp == 0) {
            temp = 17;
            temp = 31 * temp + name.hashCode();
            temp = 31 * temp + Arrays.hashCode(args);
            hash = temp;
        }

        return temp;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (obj == null || !(obj instanceof MethodSignature))
            return false;

        MethodSignature other = (MethodSignature) obj;

        if (!name.equals(other.name))
            return false;

        if (!Arrays.equals(args, other.args))
            return false;

        return true;
    }

}
