package util.old;


import java.lang.reflect.Array;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;



public class Assoc {
    private final static Set<Map.Entry<String, Assoc>> EMPTY = Collections.unmodifiableSet(new HashSet<Map.Entry<String, Assoc>>());

    private Object value;
    private Map<String, Assoc> childs;
    private int index = 0;


    /**
     * Ассоциативный массив
     */
    public Assoc() {
    }


    /**
     * Получить набор элементов массива
     *
     * @return набор элементов массива
     */
    public Set<Map.Entry<String, Assoc>> childs() {
        return this.childs != null ? this.childs.entrySet() : EMPTY;
    }


    /**
     * Получить размер массива
     *
     * @return кол-во элементов в массиве
     */
    public int size() {
        return this.childs != null ? this.childs.size() : 0;
    }


    /**
     * Является ли текущий элемент массивом
     *
     * @return true / false
     */
    public boolean isArray() {
        return this.childs != null;
    }


    /**
     * Инициализирвоать элемент массива
     *
     * @param name - имя элемента
     * @return ссылку на новый элемент массива
     */
    public Assoc ins(String name) {
        if (this.childs == null) {
            this.childs = new LinkedHashMap<String, Assoc>();
        }
        if (name == null) {
            name = String.valueOf(this.index++);

        } else if (isNumeric(name)) {
            try {
                int index = Integer.parseInt(name);
                if (index >= this.index)
                    this.index = index + 1;
            } catch (Exception e) {
            }
        }
        Assoc item = this.childs.get(name);
        if (item == null) {
            item = new Assoc();
            this.childs.put(name, item);
        }
        return item;
    }


    /**
     * Получить элемент массива
     * если элемент не существует, возвращается виртуальный (временный) элемент
     *
     * @param name - имя элемента
     * @return ссылка на элемент
     */
    public Assoc get(String name) {
        Assoc item = null;
        if (this.childs != null)
            item = this.childs.get(name);
        return item != null ? item : new Assoc();
    }


    /**
     * Установить значение элемента
     *
     * @param value - значение
     */
    public void value(Object value) {
        this.value = value;
    }


    /**
     * Поулчить значение элемента
     *
     * @return значение
     */
    public Object value() {
        return this.value;
    }


    /**
     * Добавить элемент в массив
     *
     * @param name - имя элемента
     * @param value - значение
     * @return ссылку на текущий массив
     */
    public Assoc put(String name, Object value) {
        this.ins(name).value(value);
        return this;
    }


    /**
     * Объединить массив с объектом:
     * - ассоциативным массивом,
     * - списком,
     * - картой,
     * - обычным массивом
     *
     * @param value - объект
     * @return ссылку на текущий массив
     */
    public Assoc merge(Object value) {
        if (value == null) {
            return this;
        }

        if (value instanceof Assoc) {
            Assoc temp = (Assoc) value;
            if (temp.childs != null) {
                for (Map.Entry<String, Assoc> e : temp.childs.entrySet())
                    this.ins(e.getKey()).merge(e.getValue());
            } else {
                this.value = temp.value;
            }
            return this;
        }

        if (value instanceof java.util.Map) {
            String name;
            for (Map.Entry<?, ?> c : ((Map<?, ?>) value).entrySet()) {
                name = c.getKey() != null ? c.getKey().toString() : null;
                this.ins(name).merge(c.getValue());
            }
            return this;
        }

        if (value instanceof java.lang.Iterable) {
            for (Object c : (Iterable<?>) value) {
                this.ins(null).merge(c);
            }
            return this;
        }

        if (value.getClass().isArray()) {
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                this.ins(null).merge(Array.get(value, i));
            }
            return this;
        }

        this.value = value;
        return this;
    }




    private static boolean isNumeric(String name) {
        if (name == null || name.isEmpty())
            return false;

        char c;
        for (int i = 0; i < name.length(); i++) {
            c = name.charAt(i);
            if (c >= '0' && c <= '9')
                continue;

            return false;
        }

        return true;
    }


}
