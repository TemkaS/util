/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.prop;


import java.text.DateFormat;
import java.util.Date;

import net.darkslave.util.StringParser;





/**
 *  Класс-контейнер для представления строковых значений
 */
abstract public class StringPresenter {


    abstract public String getValue();


    public Boolean getBoolean(Boolean value) {
        return StringParser.toBoolean(getValue(), value);
    }


    public Boolean getBoolean() {
        return getBoolean(null);
    }


    public Integer getInteger(Integer value) {
        return StringParser.toInteger(getValue(), value);
    }


    public Integer getInteger() {
        return getInteger(null);
    }


    public Long getLong(Long value) {
        return StringParser.toLong(getValue(), value);
    }


    public Long getLong() {
        return getLong(null);
    }


    public Double getDouble(Double value) {
        return StringParser.toDouble(getValue(), value);
    }


    public Double getDouble() {
        return getDouble(null);
    }


    public Date getDate(Date value, DateFormat format) {
        return StringParser.toDate(getValue(), value, format);
    }


    public Date getDate(DateFormat format) {
        return getDate(null, format);
    }


    public Date getDate(Date value, String format) {
        return StringParser.toDate(getValue(), value, format);
    }


    public Date getDate(String format) {
        return getDate(null, format);
    }


    public String getString(String value) {
        String param = getValue();
        return param != null ? param : value;
    }


    public String getString() {
        return getValue();
    }


    @Override
    public String toString() {
        return getValue();
    }


}
