package net.darkslave.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



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
