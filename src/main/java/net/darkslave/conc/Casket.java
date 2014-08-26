/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.conc;


import java.util.concurrent.locks.AbstractQueuedSynchronizer;




/**
 * Запираемое значение (шкатулка)
 */
public class Casket<T> {
    private final Sync<T> sync = new Sync<T>();


    /**
     * Инициализация без установленного значения (шкатулка закрыта)
     */
    public Casket() {
    }


    /**
     * Инициализация с установленным значением (шкатулка открыта)
     */
    public Casket(T value) {
        sync.set(value);
    }


    /**
     * Получить значение контейнера
     * если значение еще не установлено, поток добавляется в очередь ожидания
     */
    public T get() throws InterruptedException {
        return sync.get();
    }


    /**
     * Установить значение контейнера
     * если значение не было до этого установлено, все ожидающие потоки будут освобождены,
     * если значение уже было установлено, то вызов не имеет никакого эффекта
     */
    public void set(T value) {
        sync.set(value);
    }


    /**
     * Сбросить флаг установки (запереть шкатулку)
     */
    public void lock() {
        sync.lock();
    }


    /**
     * Установить флаг установки (открыть шкатулку)
     */
    public void unlock() {
        sync.unlock();
    }


    /**
     * Проверка флага установки
     */
    public boolean isLocked() {
        return sync.isLocked();
    }



    private static final class Sync<T> extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = -1293471815633057080L;
        private static final int NEW     = 0;
        private static final int SETTING = 1;
        private static final int SET     = 2;

        private T value;


        public T get() throws InterruptedException {
            acquireSharedInterruptibly(0);
            return value;
        }


        public void set(T value) {
            if (getState() != NEW)
                return;

            if (!compareAndSetState(NEW, SETTING))
                return;

            this.value = value;

            releaseShared(0);
        }


        public void lock() {
            setState(NEW);
        }


        public void unlock() {
            releaseShared(0);
        }


        public boolean isLocked() {
            return getState() == SET;
        }


        @Override
        protected int tryAcquireShared(int ignore) {
            return getState() == SET ? 1 : -1;
        }


        @Override
        protected boolean tryReleaseShared(int ignore) {
            setState(SET);
            return true;
        }

    }

}
