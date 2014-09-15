/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;



/**
 * Класс-сервис для работы с датами
 */
public class DateService {
    private static final ConcurrentMap<String, Queue<DateFormat>> __dateformat = new ConcurrentHashMap<String, Queue<DateFormat>>();
    private static final ConcurrentMap<String, TimeZone> __timezone = new ConcurrentHashMap<String, TimeZone>();
    private static volatile TimeZone defaultTimeZone = null;


    private static Queue<DateFormat> queue(String format) {
        Queue<DateFormat> result = __dateformat.get(format);

        if (result == null) {
            result = new ConcurrentLinkedQueue<DateFormat>();
            Queue<DateFormat> temp = __dateformat.putIfAbsent(format, result);
            if (temp != null)
                result = temp;
        }

        return result;
    }


    /**
     * Форматирование даты
     *
     * @param source   - дата
     * @param pattern  - шаблон форматирования
     * @param zone - временная зона
     * @return строку
     */
    public static String format(Date source, String pattern, TimeZone zone) {
        if (zone == null)
            throw new IllegalArgumentException("Timezone can't be null");

        DateFormat format = queue(pattern).poll();
        try {
            if (format == null)
                format = new SimpleDateFormat(pattern);

            format.setTimeZone(zone);

            return format.format(source);

        } finally {
            if (format != null)
                queue(pattern).offer(format);
        }
    }


    public static String format(Date source, String pattern, String zone) {
        return format(source, pattern, getTimeZone(zone));
    }


    public static String format(Date source, String pattern) {
        return format(source, pattern, getDefaultTimeZone());
    }


    /**
     * Разбор даты по шаблону
     *
     * @param source   - исходная строка
     * @param pattern  - шаблон форматирования
     * @param zone - временная зона
     * @return дату
     */
    public static Date parse(String source, String pattern, TimeZone zone) throws ParseException {
        if (zone == null)
            throw new IllegalArgumentException("Timezone can't be null");

        DateFormat format = queue(pattern).poll();
        try {
            if (format == null)
                format = new SimpleDateFormat(pattern);

            format.setTimeZone(zone);

            return format.parse(source);

        } finally {
            if (format != null)
                queue(pattern).offer(format);
        }
    }


    public static Date parse(String source, String pattern, String zone) throws ParseException {
        return parse(source, pattern, getTimeZone(zone));
    }


    public static Date parse(String source, String pattern) throws ParseException {
        return parse(source, pattern, getDefaultTimeZone());
    }



    /**
     * Текущая временная зона сервиса
     */
    public static TimeZone getDefaultTimeZone() {
        TimeZone result = defaultTimeZone;

        if (result == null) {
            final TimeZone temp = TimeZone.getDefault();
            result = defaultTimeZone = temp;
        }

        return result;
    }


    /**
     * Сменить текущую временную зону сервиса
     *
     * @param zone - новая временная зона
     */
    public static void setDefaultTimeZone(TimeZone zone) {
        TimeZone.setDefault(zone);
        defaultTimeZone = zone;
    }



    /**
     * Получить временную зону по ее идентификатору
     *
     * @param zone - идентификатор временной зоны
     * @return временную зону
     */
    public static TimeZone getTimeZone(String zone) {
        if (zone == null)
            return getDefaultTimeZone();

        TimeZone result = __timezone.get(zone);

        if (result == null) {
            result = TimeZone.getTimeZone(zone);
            TimeZone temp = __timezone.putIfAbsent(zone, result);
            if (temp != null)
                result = temp;
        }

        return result;
    }



    private DateService() {}
}
