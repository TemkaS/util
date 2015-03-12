/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



/**
 * Параметры сериализации типа
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JsonSerialize {

    /**
     * Набор сериализуемых полей
     */
    JsonProperty[] value() default {};

    /**
     * Метод замены исходного объекта
     */
    String replaceWith()   default "";

}
