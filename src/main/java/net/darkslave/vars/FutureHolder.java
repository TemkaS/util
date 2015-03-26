/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.vars;


import java.util.concurrent.locks.AbstractQueuedSynchronizer;




/**
 * Контейнер для значений, устанавливаемых другими потоками
 */
public class FutureHolder<T> implements Holder<T> {
    private final Sync<T> sync = new Sync<>();


    /**
     * Инициализация без установленного значения
     */
    public FutureHolder() {
    }


    /**
     * Инициализация с установленным значением
     */
    public FutureHolder(T value) {
        sync.set(value);
    }


    /**
     * Получить значение контейнера:<br>
     * если значение еще не установлено, поток добавляется в очередь ожидания
     */
    @Override
    public T get() {
        try {
            return sync.get();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(ie);
        }
    }


    /**
     * Установить значение:<br>
     * если значение не было до этого установлено, все ожидающие потоки будут освобождены,<br>
     * если значение уже было установлено, то вызов не имеет никакого эффекта
     */
    @Override
    public void set(T value) {
        sync.set(value);
    }


    /**
     * Сбросить флаг установки значения
     */
    public void reset() {
        sync.reset();
    }


    /**
     * Проверка флага установки
     */
    public boolean isset() {
        return sync.isset();
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


        public void reset() {
            setState(NEW);
        }


        public boolean isset() {
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
