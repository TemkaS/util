/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.logs;



/**
 * Абстрактный класс логгера ошибок
 */
public abstract class AbstractLogger implements Logger {

    @Override
    public void info(String content) {
        print("INFO", content, null);
    }


    @Override
    public void info(String content, Throwable e) {
        print("INFO", content, e);
    }


    @Override
    public void warn(String content) {
        print("WARN", content, null);
    }


    @Override
    public void warn(String content, Throwable e) {
        print("WARN", content, e);
    }


    @Override
    public void debug(String content) {
        print("DEBUG", content, null);
    }


    @Override
    public void debug(String content, Throwable e) {
        print("DEBUG", content, e);
    }


    @Override
    public void error(String content) {
        print("ERROR", content, null);
    }


    @Override
    public void error(String content, Throwable e) {
        print("ERROR", content, e);
    }


    protected abstract void print(String type, String content, Throwable e);

}
