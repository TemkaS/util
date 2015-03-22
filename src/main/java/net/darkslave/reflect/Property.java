/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.reflect;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;





public abstract class Property {


    public static Property create(String name, Field delegate) {
        return new FieldProperty(name, delegate);
    }


    public static Property create(String name, Method delegate) {
        return new MethodProperty(name, delegate);
    }


    /***********************************************************************************************
     */
    private final String name;


    private Property(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    public abstract Object get(Object target, Object ... args) throws ReflectiveOperationException;


    /***********************************************************************************************
     */
    private static class FieldProperty extends Property {
        private final Field delegate;


        public FieldProperty(String name, Field delegate) {
            super(name);
            this.delegate = Objects.requireNonNull(delegate, "Parameter can't be null");
        }


        @Override
        public Object get(Object target, Object ... args) throws ReflectiveOperationException {
            delegate.setAccessible(true);
            return delegate.get(target);
        }

    }


    /***********************************************************************************************
     */
    private static class MethodProperty extends Property {
        private final Method delegate;


        public MethodProperty(String name, Method delegate) {
            super(name);
            this.delegate = Objects.requireNonNull(delegate, "Parameter can't be null");
        }


        @Override
        public Object get(Object target, Object ... args) throws ReflectiveOperationException {
            delegate.setAccessible(true);
            return delegate.invoke(target, args);
        }

    }


}
