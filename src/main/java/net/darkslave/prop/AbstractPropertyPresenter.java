/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.prop;


import java.text.DateFormat;
import java.util.Date;
import net.darkslave.util.StringParser;





/**
 * Класс-контейнер для представления строковых значений
 */
public abstract class AbstractPropertyPresenter implements PropertyPresenter {


    protected abstract String getValue(String name);


    @Override
    public Boolean getBoolean(String name, Boolean value) {
        return StringParser.toBoolean(getValue(name), value);
    }


    @Override
    public Boolean getBoolean(String name) {
        return getBoolean(name, null);
    }


    @Override
    public Integer getInteger(String name, Integer value) {
        return StringParser.toInteger(getValue(name), value);
    }


    @Override
    public Integer getInteger(String name) {
        return getInteger(name, null);
    }


    @Override
    public Long getLong(String name, Long value) {
        return StringParser.toLong(getValue(name), value);
    }


    @Override
    public Long getLong(String name) {
        return getLong(name, null);
    }


    @Override
    public Double getDouble(String name, Double value) {
        return StringParser.toDouble(getValue(name), value);
    }


    @Override
    public Double getDouble(String name) {
        return getDouble(name, null);
    }


    @Override
    public Date getDate(String name, DateFormat format, Date value) {
        return StringParser.toDate(getValue(name), format, value);
    }


    @Override
    public Date getDate(String name, DateFormat format) {
        return getDate(name, format, null);
    }


    @Override
    public Date getDate(String name, String format, Date value) {
        return StringParser.toDate(getValue(name), format, value);
    }


    @Override
    public Date getDate(String name, String format) {
        return getDate(name, format, null);
    }


    @Override
    public String getString(String name, String value) {
        String param = getValue(name);
        return param != null ? param : value;
    }


    @Override
    public String getString(String name) {
        return getValue(name);
    }


}
