/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.json;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



/**
 * Определение поля / метода сериализации
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonProperty {

    /**
     * Название свойства
     */
    String value()  default "";

    /**
     * Сериализуемое поле
     */
    String field()  default "";

    /**
     * Сериализуемый метод
     */
    String method() default "";

}
