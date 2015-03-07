/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package untested;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import net.darkslave.vars.FutureHolder;






public class FutureRender<T> {
    private static final int TERMINATION_TIMEOUT = 100;

    private final Creator<T>  creator;
    private final Consumer<T> consumer;
    private final Producer<T> producer;

    private final BlockingQueue<FutureHolder<T>> consumerQueue;
    private final BlockingQueue<FutureHolder<T>> producerQueue;
    private final int producerCount;
    private final int producerLimit;

    private final ExecutorService pool;

    private final Lock lock;

    private long delay;
    private long value;

    private State state;


    public FutureRender(Producer<T> producer, int count, Consumer<T> consumer, Creator<T> creator, int limit) {
        this.creator  = creator;
        this.consumer = consumer;
        this.producer = producer;

        this.consumerQueue = new LinkedBlockingQueue<FutureHolder<T>>();
        this.producerQueue = new LinkedBlockingQueue<FutureHolder<T>>();
        this.producerCount = count;
        this.producerLimit = limit;

        this.pool  = Executors.newFixedThreadPool(count + 1);
        this.lock  = new ReentrantLock();

        this.state = State.STOPPED;
    }


    public synchronized void start(int frequency) {
        if (state == State.STARTED)
            throw new IllegalStateException();

        delay = TimeUnit.SECONDS.toNanos(1) / frequency;

        for (int i = 0; i < producerLimit; i++)
            producerQueue.add(new FutureHolder<T>(creator.create()));

        for (int i = 0; i < producerCount; i++)
            pool.execute(new ProducerWorker());

        pool.execute(new ConsumerWorker());

        state = State.STARTED;
    }


    public synchronized void stop() {
        if (state != State.STARTED)
            throw new IllegalStateException();

        try {
            pool.shutdownNow();
            pool.awaitTermination(TERMINATION_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        } finally {
            producerQueue.clear();
            consumerQueue.clear();
            value = 0L;
            state = State.STOPPED;
        }

    }


    private class ConsumerWorker implements Runnable {
        @Override
        public void run() {
            while (!Thread.interrupted()) try {
                long expired = System.nanoTime() + delay;

                FutureHolder<T> holder = consumerQueue.take();

                try {
                    consumer.consume(holder.get());
                } finally {
                    producerQueue.add(holder);
                }

                long sleep = TimeUnit.NANOSECONDS.toMillis(expired - System.nanoTime());

                if (sleep > 0)
                    Thread.sleep(sleep);

            } catch (InterruptedException e) {
                return;
            }
        }
    }


    private class ProducerWorker implements Runnable {
        @Override
        public void run() {
            while (!Thread.interrupted()) try {
                FutureHolder<T> holder = producerQueue.take();

                T data = holder.get();
                holder.reset();

                long total;

                lock.lock();
                try {
                    total = value;
                    value+= delay;
                    consumerQueue.add(holder);
                } finally {
                    lock.unlock();
                }

                producer.produce(data, total, delay);

                holder.set(data);

            } catch (InterruptedException e) {
                return;
            }
        }
    }


    public static interface Creator<T> {
        T create();
    }


    public static interface Consumer<T> {
        void consume(T value);
    }


    public static interface Producer<T> {
        void produce(T value, long total, long delay);
    }


    private static enum State {
        STARTED,
        STOPPED;
    }


}
