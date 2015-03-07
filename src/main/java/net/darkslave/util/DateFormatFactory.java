package net.darkslave.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;




/**
 * Фабрика дейт-форматтеров
 */
public interface DateFormatFactory {

    DateFormatFactory DEFAULT_FACTORY = new DateFormatFactory() {

        @Override
        public DateFormat get(String format) {
            return new SimpleDateFormat(format);
        }

    };


    DateFormat get(String format);


}
