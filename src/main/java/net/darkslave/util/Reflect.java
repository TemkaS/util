/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.util;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;





public class Reflect {
    private static Map<Class<?>, Map<String, Field>> fields = new ConcurrentHashMap<Class<?>, Map<String, Field>>();
    private static Map<Class<?>, Set<Class<?>>>      supers = new ConcurrentHashMap<Class<?>, Set<Class<?>>>();
    private static Map<Class<?>, Map<MethodSignature, Method>> methods = new ConcurrentHashMap<Class<?>, Map<MethodSignature, Method>>();




    /**
     * Получить набор полей указанного класса.
     * Возвращает набор полей указанного класса и всего его суперклассов.
     *
     * @param clazz - класс
     * @return набор полей класса
     */
    public static Map<String, Field> getFields(Class<?> clazz) {
        Map<String, Field> result = fields.get(clazz);

        if (result == null) {
            result = getFields0(clazz);
            fields.put(clazz, result);
        }

        return result;
    }


    private static Map<String, Field> getFields0(Class<?> clazz) {
        Map<String, Field> result = new HashMap<String, Field>();

        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields())
                if (!result.containsKey(field.getName()))
                    result.put(field.getName(), field);

            clazz = clazz.getSuperclass();
        }

        return result;
    }



    /**
     * Получить метод указанного класса по его сигнатуре.
     *
     * @param clazz - класс
     * @param name  - имя метода
     * @param args  - список типов аргументов
     * @return метод класса
     */
    public static Method getMethod(Class<?> clazz, String name, Class<?> ... args) {
        Map<MethodSignature, Method> result = getMethods(clazz);
        MethodSignature sign = new MethodSignature(name, args);
        return result.get(sign);
    }



    /**
     * Получить набор методов указанного класса.
     * Возвращает набор методов указанного класса и всего его суперклассов.
     *
     * @param clazz - класс
     * @return набор методов класса
     */
    public static Map<MethodSignature, Method> getMethods(Class<?> clazz) {
        Map<MethodSignature, Method> result = methods.get(clazz);

        if (result == null) {
            result = getMethods0(clazz);
            methods.put(clazz, result);
        }

        return result;
    }


    private static Map<MethodSignature, Method> getMethods0(Class<?> clazz) {
        Map<MethodSignature, Method> result = new HashMap<MethodSignature, Method>();
        MethodSignature sign;

        while (clazz != null) {
            for (Method method : clazz.getDeclaredMethods()) {
                sign = new MethodSignature(method.getName(), method.getParameterTypes());
                if (!result.containsKey(sign))
                    result.put(sign, method);
            }

            clazz = clazz.getSuperclass();
        }

        return result;
    }



    /**
     * Получить набор суперклассов указанного класса.
     * Возвращает набор суперклассов, включая указанный класс.
     *
     * @param clazz - класс
     * @return набор суперклассов
     */
    public static Set<Class<?>> getSuperTypes(Class<?> clazz) {
        Set<Class<?>> result = supers.get(clazz);

        if (result == null) {
            result = new LinkedHashSet<Class<?>>();

            result.add(clazz);
            getSuperTypes0(result, clazz);

            supers.put(clazz, result);
        }

        return result;
    }


    private static void getSuperTypes0(Set<Class<?>> result, Class<?> clazz) {
        Set<Class<?>> temp = new LinkedHashSet<Class<?>>();

        if (clazz.getSuperclass() != null)
            temp.add(clazz.getSuperclass());

        for (Class<?> c : clazz.getInterfaces())
            temp.add(c);

        result.addAll(temp);

        for (Class<?> c : temp)
            getSuperTypes0(result, c);
    }



    /**
     * Вспомогательный класс сигнатуры метода
     */
    public static final class MethodSignature {
        private final String     name;
        private final Class<?>[] args;
        private volatile int hash = 0;

        public MethodSignature(String name, Class<?>[] args) {
            this.name = name != null ? name : "";
            this.args = args != null ? args : new Class<?>[0];
        }

        public String getName() {
            return name;
        }

        public Class<?>[] getArgs() {
            return args;
        }

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



    private Reflect() {}
}
