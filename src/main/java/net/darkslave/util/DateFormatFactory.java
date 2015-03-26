package net.darkslave.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;




/**
 * Фабрика дейт-форматтеров
 */
@FunctionalInterface
public interface DateFormatFactory {

    DateFormatFactory DEFAULT_FACTORY = SimpleDateFormat::new;

    DateFormat get(String format);

}
