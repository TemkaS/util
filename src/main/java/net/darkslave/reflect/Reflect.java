/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.reflect;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;





public class Reflect {

    private static Map<Class<?>, Map<String, Field>> Fields = new ConcurrentHashMap<Class<?>, Map<String, Field>>();


    /**
     * Получить набор полей указанного класса и его суперклассов.
     *
     * @param clazz - класс
     * @return набор полей
     */
    public static Map<String, Field> getFields(Class<?> clazz) {
        final Map<String, Field> cached = Fields.get(clazz);

        if (cached != null)
            return cached;

        final Map<String, Field> result = getFields0(clazz);
        Fields.put(clazz, result);

        return result;
    }


    private static Map<String, Field> getFields0(Class<?> clazz) {
        Deque<Class<?>> deque = new LinkedList<Class<?>>();

        while (clazz != null) {
            deque.offerLast(clazz);
            clazz = clazz.getSuperclass();
        }

        Map<String, Field> result = new HashMap<String, Field>();

        while ((clazz = deque.pollLast()) != null) {
            for (Field field : clazz.getDeclaredFields())
                result.put(field.getName(), field);
        }

        return Collections.unmodifiableMap(result);
    }




    private static Map<Class<?>, Map<MethodSignature, Method>> Methods = new ConcurrentHashMap<Class<?>, Map<MethodSignature, Method>>();


    /**
     * Получить набор методов указанного класса.
     * Возвращает набор методов указанного класса и всего его суперклассов.
     *
     * @param clazz - класс
     * @return набор методов класса
     */
    public static Map<MethodSignature, Method> getMethods(Class<?> clazz) {
        final Map<MethodSignature, Method> cached = Methods.get(clazz);

        if (cached != null)
            return cached;

        final Map<MethodSignature, Method> result = getMethods0(clazz);
        Methods.put(clazz, result);

        return result;
    }


    private static Map<MethodSignature, Method> getMethods0(Class<?> clazz) {
        Deque<Class<?>> deque = new LinkedList<Class<?>>();

        while (clazz != null) {
            deque.offerLast(clazz);
            clazz = clazz.getSuperclass();
        }

        Map<MethodSignature, Method> result = new HashMap<MethodSignature, Method>();

        while ((clazz = deque.pollLast()) != null) {
            for (Method method : clazz.getDeclaredMethods()) {
                result.put(new MethodSignature(method), method);
            }
        }

        return Collections.unmodifiableMap(result);
    }



    /**
     * Найти поле указанного класса по его сигнатуре.
     *
     * @param clazz - класс
     * @param name  - имя поля
     * @return поле класса
     */
    public static Field getField(Class<?> clazz, String name) {
        return getFields(clazz).get(name);
    }


    /**
     * Найти метод указанного класса по его сигнатуре.
     *
     * @param clazz - класс
     * @param name  - имя метода
     * @param args  - список типов аргументов
     * @return метод класса
     */
    public static Method getMethod(Class<?> clazz, String name, Class<?> ... args) {
        return getMethods(clazz).get(new MethodSignature(name, args));
    }




    private Reflect() {}
}
