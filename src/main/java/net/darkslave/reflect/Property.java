/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;





abstract public class Property {


    public static Property create(Field delegate) {
        return new FieldProperty(delegate);
    }


    public static Property create(Method delegate) {
        return new MethodProperty(delegate);
    }


    private Property() {}


    abstract public Object get(Object target, Object... args) throws ReflectiveOperationException;


    private static class FieldProperty extends Property {
        private final Field delegate;

        public FieldProperty(Field delegate) {
            if (delegate == null)
                throw new IllegalArgumentException("Parameter can't be null");
            this.delegate = delegate;
        }

        @Override
        public Object get(Object target, Object... args) throws ReflectiveOperationException {
            delegate.setAccessible(true);
            return delegate.get(target);
        }

    }


    private static class MethodProperty extends Property {
        private final Method delegate;

        public MethodProperty(Method delegate) {
            if (delegate == null)
                throw new IllegalArgumentException("Parameter can't be null");
            this.delegate = delegate;
        }

        @Override
        public Object get(Object target, Object... args) throws ReflectiveOperationException {
            delegate.setAccessible(true);
            return delegate.invoke(target, args);
        }

    }


}
