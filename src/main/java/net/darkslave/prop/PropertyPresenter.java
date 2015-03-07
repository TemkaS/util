/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.prop;


import java.text.DateFormat;
import java.util.Date;
import java.util.Set;





/**
 * Класс-контейнер для представления строковых значений
 */
public interface PropertyPresenter {


    Set<String> getNames();


    Boolean getBoolean(String name, Boolean value);


    Boolean getBoolean(String name);


    Integer getInteger(String name, Integer value);


    Integer getInteger(String name);


    Long getLong(String name, Long value);


    Long getLong(String name);


    Double getDouble(String name, Double value);


    Double getDouble(String name);


    Date getDate(String name, DateFormat format, Date value);


    Date getDate(String name, DateFormat format);


    Date getDate(String name, String format, Date value);


    Date getDate(String name, String format);


    String getString(String name, String value);


    String getString(String name);


}
