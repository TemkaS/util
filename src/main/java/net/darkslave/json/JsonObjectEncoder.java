/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.json;

import java.io.IOException;



/**
 * Интерфейс кастомного сериализатора
 */
public abstract class JsonObjectEncoder {
    private final Class<?> targetClass;


    protected JsonObjectEncoder(Class<?> targetClass) {
        this.targetClass = targetClass;
    }


    /**
     * Целевой класс
     */
    public Class<?> targetClass() {
        return targetClass;
    }


    /**
     * Метод для кодирования
     */
    public abstract void encode(JsonEncoder encoder, Object value, int level) throws IOException;

}

