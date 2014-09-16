/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.logs;



/**
 * Интерфейс логгера ошибок
 */
public interface Logger {

    void info(String content);


    void info(String content, Throwable e);


    void warn(String content);


    void warn(String content, Throwable e);


    void debug(String content);


    void debug(String content, Throwable e);


    void error(String content);


    void error(String content, Throwable e);

}
