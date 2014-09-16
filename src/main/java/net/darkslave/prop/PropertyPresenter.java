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
abstract public class PropertyPresenter {


    abstract public String getValue(String name);


    public PropertyPresenter getChild(String prefix) {
        return new ChildPresenter(this, prefix);
    }


    public Boolean getBoolean(String name, Boolean value) {
        return StringParser.toBoolean(getValue(name), value);
    }


    public Boolean getBoolean(String name) {
        return getBoolean(name, null);
    }


    public Integer getInteger(String name, Integer value) {
        return StringParser.toInteger(getValue(name), value);
    }


    public Integer getInteger(String name) {
        return getInteger(name, null);
    }


    public Long getLong(String name, Long value) {
        return StringParser.toLong(getValue(name), value);
    }


    public Long getLong(String name) {
        return getLong(name, null);
    }


    public Double getDouble(String name, Double value) {
        return StringParser.toDouble(getValue(name), value);
    }


    public Double getDouble(String name) {
        return getDouble(name, null);
    }


    public Date getDate(String name, Date value, DateFormat format) {
        return StringParser.toDate(getValue(name), value, format);
    }


    public Date getDate(String name, DateFormat format) {
        return getDate(name, null, format);
    }


    public Date getDate(String name, Date value, String format) {
        return StringParser.toDate(getValue(name), value, format);
    }


    public Date getDate(String name, String format) {
        return getDate(name, null, format);
    }


    public String getString(String name, String value) {
        String param = getValue(name);
        return param != null ? param : value;
    }


    public String getString(String name) {
        return getValue(name);
    }


    /**
     * Дочерний представитель
     */
    private static class ChildPresenter extends PropertyPresenter {
        private final PropertyPresenter parent;
        private final String prefix;


        public ChildPresenter(PropertyPresenter parent, String prefix) {
            if (parent == null)
                throw new IllegalArgumentException("Parent can't be null");

            if (prefix == null)
                throw new IllegalArgumentException("Prefix can't be null");

            this.parent = parent;
            this.prefix = prefix;
        }


        @Override
        public String getValue(String name) {
            return parent.getValue(prefix + name);
        }

    }

}
