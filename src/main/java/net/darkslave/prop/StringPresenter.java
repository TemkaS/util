/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.prop;


import java.text.DateFormat;
import java.util.Date;





/**
 *  Класс-контейнер для представления строковых значений
 */
public interface StringPresenter {


    Boolean getBoolean(Boolean value);


    Boolean getBoolean();


    Integer getInteger(Integer value);


    Integer getInteger();


    Long getLong(Long value);


    Long getLong();


    Double getDouble(Double value);


    Double getDouble();


    Date getDate(DateFormat format, Date value);


    Date getDate(DateFormat format);


    Date getDate(String format, Date value);


    Date getDate(String format);


    String getString(String value);


    String getString();


}
