package util_old;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import util.resc.ResourceException;
import util.resc.ResourceHolder;
import util.resc.ResourceInterruptedException;
import util.resc.ResourcePool;
import util.resc.ResourceService;






public class ProducerService<T> {


    public static interface Consumer<T> {
        void consume(T value);
    }


    public static interface Producer<T> {
        void produce(T value);
    }


    public static enum State {
        NEW,
        STARTED,
        STOPPED;
    }

    private static final int TERMINATION_TIMEOUT = 100;

    private final Consumer<T> consumer;
    private final Producer<T> producer;

    private final BlockingQueue<ResourceHolder<T>> consumerQueue;
    private final ResourcePool<T>  producerQueue;

    private final ExecutorService  workerPool;
    private final int workerPoolSize;

    private State state;


    public ProducerService(ResourceService<T> service, int capacity, Producer<T> producer, int size, Consumer<T> consumer) {
        this.consumer = consumer;
        this.producer = producer;

        this.workerPoolSize = size;
        this.workerPool     = Executors.newFixedThreadPool(size);

        this.consumerQueue  = new LinkedBlockingQueue<ResourceHolder<T>>();
        this.producerQueue  = new ResourcePool<T>(service, capacity);

        this.state = State.NEW;
    }


    private class ProducerWorker implements Runnable {
        @Override
        public void run() {
            while (!Thread.interrupted()) try {
                ResourceHolder<T> holder = producerQueue.acquire();

                try {
                    producer.produce(holder.get());
                } finally {
                    consumerQueue.add(holder);
                }

            } catch (ResourceInterruptedException e) {
                return;

            } catch (ResourceException e) {
                e.printStackTrace();
            }
        }
    }


    public void consume() throws ResourceException {
        try {
            ResourceHolder<T> holder = consumerQueue.take();
            try {
                consumer.consume(holder.get());
            } finally {
                holder.close();
            }
        } catch (InterruptedException ie) {
            throw ResourceException.wrap(ie);
        }
    }


    public synchronized void start() {
        if (state == State.STARTED)
            return;

        state = State.STARTED;

        int index = workerPoolSize;

        while (--index >= 0)
            workerPool.execute(new ProducerWorker());

    }


    public synchronized void stop() throws ResourceException {
        ResourceException error = null;

        if (state != State.STARTED)
            return;

        state = State.STOPPED;

        workerPool.shutdownNow();

        try {
            workerPool.awaitTermination(TERMINATION_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ie) {
            error = ResourceException.wrap(ie);
        }

        while (true) try {
            ResourceHolder<T> holder = consumerQueue.poll();

            if (holder == null)
                break;

            holder.close();

        } catch (Exception e) {
            error = ResourceException.wrap(error, e);
        }

        if (error != null)
            throw error;

    }


}
