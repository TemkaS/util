/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.reflect;


import java.lang.reflect.Field;
import java.lang.reflect.Method;




/**
 * Обертка над полями и методами класса
 */
public abstract class Property {


    /**
     * Создать свойство по полю класса
     */
    public static Property from(Field delegate) {
        return new FieldProperty(delegate);
    }


    /**
     * Создать свойство по имени и полю класса
     */
    public static Property from(String name, Field delegate) {
        return new FieldProperty(name, delegate);
    }


    /**
     * Создать свойство по методу класса
     */
    public static Property from(Method delegate) {
        return new MethodProperty(delegate);
    }


    /**
     * Создать свойство по имени и методу класса
     */
    public static Property from(String name, Method delegate) {
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


        public FieldProperty(Field delegate) {
            super(delegate.getName());
            this.delegate = delegate;
        }


        public FieldProperty(String name, Field delegate) {
            super(name);
            this.delegate = delegate;
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


        public MethodProperty(Method delegate) {
            super(delegate.getName());
            this.delegate = delegate;
        }


        public MethodProperty(String name, Method delegate) {
            super(name);
            this.delegate = delegate;
        }


        @Override
        public Object get(Object target, Object ... args) throws ReflectiveOperationException {
            delegate.setAccessible(true);
            return delegate.invoke(target, args);
        }

    }


}
