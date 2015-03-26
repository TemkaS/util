/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.util;


import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * Абстрактный класс для разбора строк
 */
public abstract class StringParser<T> {

    public abstract T parse(String source, T value);


    /**
     * Парсер строк как булевых значений
     */
    public static final StringParser<Boolean> BOOLEAN = new StringParser<Boolean>() {
                @Override
                public Boolean parse(String source, Boolean value) {
                    return toBoolean(source, value);
                }
            };

    /**
     * Парсер строк как целых значений
     */
    public static final StringParser<Integer> INTEGER = new StringParser<Integer>() {
                @Override
                public Integer parse(String source, Integer value) {
                    return toInteger(source, value);
                }
            };

    /**
     * Парсер строк как целых значений
     */
    public static final StringParser<Long> LONG = new StringParser<Long>() {
                @Override
                public Long parse(String source, Long value) {
                    return toLong(source, value);
                }
            };

    /**
     * Парсер строк как вещественных значений
     */
    public static final StringParser<Double> DOUBLE = new StringParser<Double>() {
                @Override
                public Double parse(String source, Double value) {
                    return toDouble(source, value);
                }
            };


    /**
     * Распарсить список строк, используя парсер
     *
     * @param source  - список строк
     * @param parser  - парсер строк
     * @param value   - значение по умолчанию
     * @param notNull - добавлять нулл знаечния или нет
     * @return список результирующих значений
     */
    public static <T> List<T> parse(Iterable<String> source, StringParser<T> parser, T value, boolean notNull) {
        List<T> result = new ArrayList<>();

        for (String item : source) {
            T parsed = parser.parse(item, value);
            if (parsed != null || !notNull)
                result.add(parsed);
        }

        return result;
    }



    /**
     * преобразовать строку в булево значение
     *
     * @param source - исходная строка
     * @param value  - значение по умолчанию
     * @return булево значение
     */
    public static Boolean toBoolean(String source, Boolean value) {
        try {
            if (isEmpty(source))
                return value;

            if (source.equalsIgnoreCase("true")  || source.equalsIgnoreCase("on"))
                return Boolean.TRUE;

            if (source.equalsIgnoreCase("false") || source.equalsIgnoreCase("off"))
                return Boolean.FALSE;

            return Integer.parseInt(source) != 0;

        } catch (NumberFormatException e) {
            return value;
        }
    }


    /**
     * преобразовать в число
     *
     * @param source - исходная строка
     * @param value  - значение по умолчанию
     * @return число
     */
    public static Integer toInteger(String source, Integer value) {
        try {
            if (isEmpty(source))
                return value;

            return Integer.parseInt(clearDot(source));

        } catch (NumberFormatException e) {
            return value;
        }
    }


    /**
     * преобразовать в число
     *
     * @param source - исходная строка
     * @param value  - значение по умолчанию
     * @return число
     */
    public static Long toLong(String source, Long value) {
        try {
            if (isEmpty(source))
                return value;

            return Long.parseLong(clearDot(source));

        } catch (NumberFormatException e) {
            return value;
        }
    }


    private static String clearDot(String source) {
        int index = source.indexOf('.');
        if (index >= 0)
            return source.substring(0, index);
        return source;
    }


    /**
     * преобразовать в число
     *
     * @param source - исходная строка
     * @param value  - значение по умолчанию
     * @return число
     */
    public static Double toDouble(String source, Double value) {
        try {
            if (isEmpty(source))
                return value;

            return Double.parseDouble(source);

        } catch (NumberFormatException e) {
            return value;
        }
    }


    /**
     * преобразовать в дату согласно формата
     *
     * @param source - исходная строка
     * @param value  - значение по умолчанию
     * @param format - формат даты
     * @return дата
     */
    public static Date toDate(String source, DateFormat format, Date value) {
        try {
            if (isEmpty(source))
                return value;

            if (format == null)
                throw new IllegalArgumentException("Date format can't be null");

            return format.parse(source);

        } catch (ParseException e) {
            return value;
        }
    }


    /**
     * преобразовать в дату согласно формата
     *
     * @param source - исходная строка
     * @param value  - значение по умолчанию
     * @param format - формат даты
     * @return дата
     */
    public static Date toDate(String source, String format, Date value) {
        return toDate(source, dateFormatFactory.get(format), value);
    }



    private static volatile DateFormatFactory dateFormatFactory = DateFormatFactory.DEFAULT_FACTORY;


    /**
     * Получить текущую фабрику дейт-форматтеров
     */
    public static DateFormatFactory getDateFormatFactory() {
        return dateFormatFactory;
    }


    /**
     * Установить текущую фабрику дейт-форматтеров
     */
    public static void setDateFormatFactory(DateFormatFactory factory) {
        if (factory == null)
            throw new IllegalArgumentException("Parameter can't be null");
        dateFormatFactory = factory;
    }



    /**
     * Проверка строки на пустоту

     * @param source - строка источник
     * @return true / false
     */
    public static boolean isEmpty(CharSequence source) {
        return source == null || source.length() == 0;
    }


}

