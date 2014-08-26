package util.tm;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;






public class Counter {
    private final Lock lock = new ReentrantLock();

    private final String title;
    private final long   delta;

    private long last;
    private long count;
    private long value;


    public Counter(String title, long time, TimeUnit unit) {
        this.title = title;
        this.delta = unit.toNanos(time);
        this.last  = System.nanoTime();
    }


    public void count() {
        lock.lock();
        try {
            count++;

            normalize();

        } finally {
            lock.unlock();
        }
    }


    public long value() {
        lock.lock();
        try {
            normalize();

            return value;

        } finally {
            lock.unlock();
        }
    }


    private void normalize() {
        long next = System.nanoTime();

        if (next - last >= delta) {
            value = count;
            count = 0;
            last  = next;
        }

    }


    @Override
    public String toString() {
        return title + ": " + value();
    }


}
