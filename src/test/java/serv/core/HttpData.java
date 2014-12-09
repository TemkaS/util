package serv.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import net.darkslave.prop.StringPresenter;
import net.darkslave.util.Misc;
import net.darkslave.util.StringParser;





public class HttpData extends StringPresenter {
    private static final HttpData EMPTY = new HttpData();

    private Map<String, HttpData> array;
    private String value;
    private long   index;


    private HttpData() {}


    /**
     *  Проверить, установлено ли значение
     */
    public boolean isset() {
        return value != null || array != null;
    }


    /**
     *  Проверить, является ли элемент массивом
     */
    public boolean isArray() {
        return array != null;
    }


    /**
     *  Получить размер массива элементов
     */
    public int size() {
        if (array == null)
            throw new UnsupportedOperationException("Parameter is not an array");
        return array.size();
    }


    /**
     * Получить набор элементов массива
     */
    public Set<Map.Entry<String, HttpData>> entries() {
        if (array == null)
            throw new UnsupportedOperationException("Parameter is not an array");
        return array.entrySet();
    }


    /**
     * Получить набор ключей массива
     */
    public Set<String> keys() {
        if (array == null)
            throw new UnsupportedOperationException("Parameter is not an array");
        return array.keySet();
    }


    @Override
    public String getValue() {
        if (array != null)
            throw new UnsupportedOperationException("Parameter is not scalar");
        return value;
    }


    /**
     *  Получить вложенный элемент
     */
    public HttpData get(String ... path) {
        HttpData result = this;

        for (String item : path)
            result = result.get(item);

        return result;
    }


    /**
     *  Получить вложенный элемент
     */
    public HttpData get(String name) {
        if (array == null)
            throw new UnsupportedOperationException("Parameter is not an array");

        HttpData result = array.get(name);

        if (result == null)
            return EMPTY;

        return result;
    }


    private void setValue(List<String> path, List<String> data) throws ParameterException {
        int last = path.size() - 1;

        boolean multiple = Misc.isEmpty(path.get(last));

        if (multiple)
            last--;

        HttpData result = this;

        for (int i = 0; i <= last; i++)
            result = result.insert(path.get(i));

        if (multiple) {
            for (String item : data)
                result.insert(null).setValue(item);
            return;
        }

        if (data.size() >= 2)
            throw new ParameterException("Parameter has several values");

        result.setValue(data.get(0));
    }


    private void setValue(String value) {
        this.value = value;
    }


    private HttpData insert(String name) throws ParameterException {
        if (value != null)
            throw new ParameterException("Parameter already exists");

        if (array == null)
            array = new TreeMap<String, HttpData>();

        String   index  = index(name);
        HttpData result = array.get(index);

        if (result == null) {
            result = new HttpData();
            array.put(index, result);
        }

        return result;
    }


    private String index(String name) {
        if (Misc.isEmpty(name))
            return String.valueOf(index++);

        Long temp = StringParser.toLong(name, null);

        if (temp != null && temp.longValue() >= index)
            index = temp.longValue() + 1;

        return name;
    }



    /**
     *  Разобрать параметры запроса
     */
    public static HttpData parse(Map<String, List<String>> params) throws ParameterException {
        HttpData result = new HttpData();

        for (Map.Entry<String, List<String>> e : params.entrySet()) {
            try {
                result.setValue(parseName(e.getKey()), e.getValue());
            } catch (ParameterException pe) {
                throw new ParameterException("Parameter `" + e.getKey() + "`: " + pe.getMessage(), pe);
            }
        }

        return result;
    }



    /**
     *  Разбор имени параметра на части: a[b][c]  »  [ a, b, c ]
     */
    private static List<String> parseName(String name) {
        List<String> result = new ArrayList<String>(16);

        while (true) {
            int begin = name.indexOf('[');
            int close = name.indexOf(']', begin);

            if (begin >= 0 && close >= 0) {
                result.add(name.substring(0, begin));
                name = name.substring(begin + 1, close) + name.substring(close + 1);
            } else {
                result.add(name);
                return result;
            }

        }

    }



    public static class ParameterException extends Exception {
        private static final long serialVersionUID = 515705990778876818L;


        public ParameterException(String message, Throwable cause) {
            super(message, cause);
        }


        public ParameterException(String message) {
            super(message);
        }

    }


}
