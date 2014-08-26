/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.conc;


import java.util.concurrent.locks.AbstractQueuedSynchronizer;



/**
 * Защелка
 */
public class Latch {
    private final Sync sync = new Sync();


    /**
     * Ожидать открытия защелки
     */
    public void await() throws InterruptedException {
        sync.await();
    }


    /**
     * Закрыть защелку
     */
    public void lock() {
        sync.lock();
    }


    /**
     * Открыть защелку
     */
    public void unlock() {
        sync.unlock();
    }


    /**
     * Проверка закрытости
     */
    public boolean isLocked() {
        return sync.isLocked();
    }


    /**********************************************************************************************
     */
    private static final class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 9221134429980020596L;
        private static final int FREE = 0;
        private static final int LOCK = 1;


        public void await() throws InterruptedException {
            acquireSharedInterruptibly(0);
        }


        public void lock() {
            setState(LOCK);
        }


        public void unlock() {
            releaseShared(0);
        }


        public boolean isLocked() {
            return getState() != FREE;
        }


        @Override
        protected int tryAcquireShared(int ignore) {
            return getState() == FREE ? 1 : -1;
        }


        @Override
        protected boolean tryReleaseShared(int ignore) {
            setState(FREE);
            return true;
        }

    }

}
