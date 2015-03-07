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
public abstract class AbstractStringPresenter implements StringPresenter {


    protected abstract String getValue();


    @Override
    public Boolean getBoolean(Boolean value) {
        return StringParser.toBoolean(getValue(), value);
    }


    @Override
    public Boolean getBoolean() {
        return getBoolean(null);
    }


    @Override
    public Integer getInteger(Integer value) {
        return StringParser.toInteger(getValue(), value);
    }


    @Override
    public Integer getInteger() {
        return getInteger(null);
    }


    @Override
    public Long getLong(Long value) {
        return StringParser.toLong(getValue(), value);
    }


    @Override
    public Long getLong() {
        return getLong(null);
    }


    @Override
    public Double getDouble(Double value) {
        return StringParser.toDouble(getValue(), value);
    }


    @Override
    public Double getDouble() {
        return getDouble(null);
    }


    @Override
    public Date getDate(DateFormat format, Date value) {
        return StringParser.toDate(getValue(), format, value);
    }


    @Override
    public Date getDate(DateFormat format) {
        return getDate(format, null);
    }


    @Override
    public Date getDate(String format, Date value) {
        return StringParser.toDate(getValue(), format, value);
    }


    @Override
    public Date getDate(String format) {
        return getDate(format, null);
    }


    @Override
    public String getString(String value) {
        String param = getValue();
        return param != null ? param : value;
    }


    @Override
    public String getString() {
        return getValue();
    }


    @Override
    public String toString() {
        return getValue();
    }


}
